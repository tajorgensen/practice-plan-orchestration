package com.hbwj.domain.useCase;

import com.hbwj.adapter.model.PracticePlanGenerateRequest;
import com.hbwj.domain.model.Drill;
import com.hbwj.domain.model.DrillEquipment;
import com.hbwj.domain.model.FocusArea;
import com.hbwj.domain.model.Position;
import com.hbwj.domain.port.DrillEquipmentService;
import com.hbwj.domain.port.DrillService;
import com.hbwj.domain.port.PositionService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Helper class responsible for finding and selecting appropriate drills
 * for different sections of the practice plan.
 */
@Component
public class PracticePlanDrillSelector {

    /**
     * Find drills that match the criteria in the request
     */
    public List<Drill> findSuitableDrills(
            PracticePlanGenerateRequest requestDto,
            DrillService drillService,
            PositionService positionService,
            DrillEquipmentService drillEquipmentService) {

        List<Drill> drills;

        // Base query on sport ID
        drills = drillService.getDrillsBySportId(requestDto.getSportId());

        // Filter by focus area if specified
        if (requestDto.getFocusAreaId() != null) {
            drills = drills.stream()
                    .filter(d -> d.getFocusAreaId().equals(requestDto.getFocusAreaId()))
                    .collect(Collectors.toList());
        }

        // Filter by position if specified
        if (requestDto.getPositionId() != null) {
            Position position = positionService.getPositionById(requestDto.getPositionId());

            drills = drills.stream()
                    .filter(d -> d.getPositionIds().contains(position.getId()))
                    .collect(Collectors.toList());
        }

        // Filter by age group if specified (replacing difficulty level)
        if (requestDto.getAgeGroup() != null && !requestDto.getAgeGroup().isEmpty()) {
            // Map age groups to appropriate difficulty levels
            String difficultyLevel;
            switch (requestDto.getAgeGroup()) {
                case "6-8":
                    difficultyLevel = "Beginner";
                    break;
                case "9-11":
                case "12-14":
                    difficultyLevel = "Intermediate";
                    break;
                case "15-17":
                case "18+":
                    difficultyLevel = "Advanced";
                    break;
                default:
                    difficultyLevel = null;
            }

            if (difficultyLevel != null) {
                final String finalDifficultyLevel = difficultyLevel;
                drills = drills.stream()
                        .filter(d -> d.getDifficultyLevel() != null &&
                                d.getDifficultyLevel().equalsIgnoreCase(finalDifficultyLevel))
                        .collect(Collectors.toList());
            }
        }

        // Filter out drills that are too long for even one station
        int maxDrillDuration = requestDto.getTotalDurationMinutes() - requestDto.getWarmupDurationMinutes();
        drills = drills.stream()
                .filter(d -> d.getDurationMinutes() == null || d.getDurationMinutes() <= maxDrillDuration)
                .collect(Collectors.toList());

        // Handle equipment limitations if specified
        if (requestDto.getMaxEquipmentTypes() != null && requestDto.getMaxEquipmentTypes() > 0) {
            drills = drills.stream()
                    .filter(drill -> {
                        List<DrillEquipment> equipment = drillEquipmentService.getDrillEquipmentByDrillId(drill.getId());
                        return equipment.size() <= requestDto.getMaxEquipmentTypes();
                    })
                    .collect(Collectors.toList());
        }

        return drills;
    }

    /**
     * Select a random drill from the list, ensuring it hasn't been used before.
     * Returns null if no unused drills are available.
     */
    public Drill selectRandomDrill(List<Drill> drills, Set<Long> usedDrillIds) {
        List<Drill> availableDrills = drills.stream()
                .filter(d -> !usedDrillIds.contains(d.getId()))
                .collect(Collectors.toList());

        if (availableDrills.isEmpty()) {
            return null; // No unused drills available
        }

        return availableDrills.get(new Random().nextInt(availableDrills.size()));
    }

    /**
     * Select the best drill for a particular focus area that hasn't been used yet.
     * Returns null if no unused drills are available.
     */
    public Drill selectBestDrill(List<Drill> drills, FocusArea targetFocusArea, Set<Long> usedDrillIds) {
        // Filter out used drills
        List<Drill> unusedDrills = drills.stream()
                .filter(d -> !usedDrillIds.contains(d.getId()))
                .collect(Collectors.toList());

        if (unusedDrills.isEmpty()) {
            return null; // No unused drills available
        }

        if (targetFocusArea != null) {
            // Prioritize drills that match the target focus area
            List<Drill> matchingDrills = unusedDrills.stream()
                    .filter(d -> d.getFocusAreaId().equals(targetFocusArea.getId()))
                    .collect(Collectors.toList());

            if (!matchingDrills.isEmpty()) {
                return matchingDrills.get(new Random().nextInt(matchingDrills.size()));
            }
        }

        // If no matching focus area or no target, pick a random unused drill
        return unusedDrills.get(new Random().nextInt(unusedDrills.size()));
    }

    /**
     * Find a generic drill for a position when no specific drill exists
     */
    public Drill findGenericDrillForPosition(
            Position position,
            List<Drill> focusAreaDrills,
            int duration,
            Set<Long> usedDrillIds) {

        // First try to find any drill that's not used yet and at least 5 minutes long
        List<Drill> availableDrills = focusAreaDrills.stream()
                .filter(d -> d.getDurationMinutes() != null && d.getDurationMinutes() >= 5 && d.getDurationMinutes() <= duration)
                .filter(d -> !usedDrillIds.contains(d.getId()))
                .collect(Collectors.toList());

        if (!availableDrills.isEmpty()) {
            return availableDrills.get(new Random().nextInt(availableDrills.size()));
        }

        // If all drills are used, allow reuse of drills at least 5 minutes long
        List<Drill> reusableDrills = focusAreaDrills.stream()
                .filter(d -> d.getDurationMinutes() != null && d.getDurationMinutes() >= 5 && d.getDurationMinutes() <= duration)
                .collect(Collectors.toList());

        if (!reusableDrills.isEmpty()) {
            return reusableDrills.get(new Random().nextInt(reusableDrills.size()));
        }

        return null;
    }

    /**
     * Generate coaching points for a drill
     */
    public String generateCoachingPoints(Drill drill) {
        StringBuilder points = new StringBuilder();

        // Extract key points from the drill description if available
        if (drill.getDescription() != null && !drill.getDescription().isEmpty()) {
            String[] sentences = drill.getDescription().split("\\.");
            if (sentences.length > 0) {
                points.append("Key focus: ").append(sentences[0].trim()).append(".");
            }
        }

        // Add specific instructions if available
        if (drill.getInstructions() != null && !drill.getInstructions().isEmpty()) {
            if (points.length() > 0) {
                points.append("\n\n");
            }

            // Extract first 1-2 sentences of instructions to highlight key elements
            String[] instructions = drill.getInstructions().split("\\.");
            if (instructions.length > 0) {
                points.append("Instructions: ").append(instructions[0].trim()).append(".");

                if (instructions.length > 1) {
                    points.append(" ").append(instructions[1].trim()).append(".");
                }
            }
        }

        // Default text if no points could be extracted
        if (points.length() == 0) {
            points.append("Focus on proper technique and form. Provide individual feedback.");
        }

        return points.toString();
    }
}