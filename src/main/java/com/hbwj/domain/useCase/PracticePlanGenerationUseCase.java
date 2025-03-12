package com.hbwj.domain.useCase;

import com.hbwj.adapter.model.PracticePlanGenerateRequest;
import com.hbwj.adapter.model.PracticePlanGenerateResponse;
import com.hbwj.adapter.model.PracticePlanSection;
import com.hbwj.adapter.model.Station;
import com.hbwj.adapter.model.StationGroup;
import com.hbwj.domain.model.Drill;
import com.hbwj.domain.model.DrillEquipment;
import com.hbwj.domain.model.FocusArea;
import com.hbwj.domain.model.Position;
import com.hbwj.domain.model.PracticePlanEquipmentSummary;
import com.hbwj.domain.model.Sport;
import com.hbwj.domain.port.DrillEquipmentService;
import com.hbwj.domain.port.DrillService;
import com.hbwj.domain.port.FocusAreaService;
import com.hbwj.domain.port.PositionService;
import com.hbwj.domain.port.SportService;
import com.hbwj.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class PracticePlanGenerationUseCase {

    private final SportService sportService;
    private final FocusAreaService focusAreaService;
    private final DrillService drillService;
    private final DrillEquipmentService drillEquipmentService;
    private final PositionService positionService;

    public PracticePlanGenerateResponse execute(PracticePlanGenerateRequest requestDto) {
        Sport sport = sportService.getSportById(requestDto.getSportId());

        FocusArea focusArea = null;
        if (requestDto.getFocusAreaId() != null) {
            focusArea = focusAreaService.getFocusAreaById(requestDto.getFocusAreaId());
        }

        // Validate and normalize time parameters
        validateTimeParameters(requestDto);

        // Find suitable drills
        List<Drill> availableDrills = findSuitableDrills(requestDto);

        if (availableDrills.isEmpty()) {
            throw new ResourceNotFoundException("No suitable drills found for the specified criteria");
        }

        // Generate response
        PracticePlanGenerateResponse responseDto = new PracticePlanGenerateResponse();
        responseDto.setName(generatePlanName(sport, focusArea));
        responseDto.setDescription(generatePlanDescription(sport, focusArea, requestDto));
        responseDto.setSportName(sport.getName());
        responseDto.setFocusAreaName(focusArea != null ? focusArea.getName() : "Mixed");
        responseDto.setTotalDurationMinutes(requestDto.getTotalDurationMinutes());

        List<PracticePlanSection> sections = new ArrayList<>();
        Set<Long> usedDrillIds = new HashSet<>();

        // Add warmup section
        PracticePlanSection warmupSection = createWarmupSection(requestDto, availableDrills, usedDrillIds);
        if (warmupSection != null) {
            sections.add(warmupSection);
        }

        // Add station sections with rotations
        if (requestDto.getStationTotalDurationMinutes() != null && requestDto.getStationTotalDurationMinutes() > 0) {
            PracticePlanSection stationsSection = createStationsSection(requestDto, availableDrills, usedDrillIds);
            if (stationsSection != null) {
                sections.add(stationsSection);
            }
        }

        // Add position group section if specified
        if (requestDto.getPositionGroupDurationMinutes() != null && requestDto.getPositionGroupDurationMinutes() > 0) {
            PracticePlanSection positionGroupSection = createPositionGroupSection(requestDto, availableDrills, usedDrillIds);
            if (positionGroupSection != null) {
                sections.add(positionGroupSection);
            }
        }

        // Add team time section if specified
        if (requestDto.getTeamTimeDurationMinutes() != null && requestDto.getTeamTimeDurationMinutes() > 0) {
            PracticePlanSection teamTimeSection = createTeamTimeSection(requestDto, availableDrills, usedDrillIds);
            if (teamTimeSection != null) {
                sections.add(teamTimeSection);
            }
        }

        responseDto.setSections(sections);

        // Calculate equipment needed
        responseDto.setEquipmentNeeded(calculateEquipmentNeeded(sections));

        return responseDto;
    }

    private List<Drill> findSuitableDrills(PracticePlanGenerateRequest requestDto) {
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

    private Drill selectRandomDrill(List<Drill> drills, Set<Long> usedDrillIds) {
        List<Drill> availableDrills = drills.stream()
                .filter(d -> !usedDrillIds.contains(d.getId()))
                .collect(Collectors.toList());

        if (availableDrills.isEmpty() && !drills.isEmpty()) {
            // If all drills have been used but we need another, just pick any
            return drills.get(new Random().nextInt(drills.size()));
        } else if (availableDrills.isEmpty()) {
            return null;
        }

        return availableDrills.get(new Random().nextInt(availableDrills.size()));
    }

    private Drill selectBestDrill(List<Drill> drills, FocusArea targetFocusArea) {
        if (targetFocusArea != null) {
            // Prioritize drills that match the target focus area
            List<Drill> matchingDrills = drills.stream()
                    .filter(d -> d.getFocusAreaId().equals(targetFocusArea.getId()))
                    .toList();

            if (!matchingDrills.isEmpty()) {
                return matchingDrills.get(new Random().nextInt(matchingDrills.size()));
            }
        }

        // If no matching focus area or no target, pick a random drill
        return drills.get(new Random().nextInt(drills.size()));
    }

    private String generatePlanName(Sport sport, FocusArea focusArea) {
        StringBuilder name = new StringBuilder();
        name.append(sport.getName());

        if (focusArea != null) {
            name.append(" - ").append(focusArea.getName());
        }

        name.append(" Practice Plan");

        // Add a timestamp to make it unique
        name.append(" (").append(String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))).append(")");

        return name.toString();
    }

    private String generateCoachingPoints(Drill drill) {
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

    /**
     * Validate that the time parameters make sense and don't exceed total practice time
     */
    private void validateTimeParameters(PracticePlanGenerateRequest requestDto) {
        int allocatedTime = requestDto.getWarmupDurationMinutes();

        // Add station time if specified
        if (requestDto.getStationTotalDurationMinutes() != null) {
            allocatedTime += requestDto.getStationTotalDurationMinutes();
        }

        // Add position group time if specified
        if (requestDto.getPositionGroupDurationMinutes() != null) {
            allocatedTime += requestDto.getPositionGroupDurationMinutes();
        }

        // Add team time if specified
        if (requestDto.getTeamTimeDurationMinutes() != null) {
            allocatedTime += requestDto.getTeamTimeDurationMinutes();
        }

        if (allocatedTime > requestDto.getTotalDurationMinutes()) {
            throw new IllegalArgumentException(
                    "The sum of warmup, station, position group, and team time durations (" +
                            allocatedTime + " min) exceeds the total practice duration (" +
                            requestDto.getTotalDurationMinutes() + " min)");
        }

        // Validate station rotation parameters
        if (requestDto.getStationTotalDurationMinutes() != null && requestDto.getStationTotalDurationMinutes() > 0) {
            if (requestDto.getStationRotationMinutes() == null || requestDto.getStationRotationMinutes() <= 0) {
                // Default to total station time / number of stations if not specified
                requestDto.setStationRotationMinutes(
                        requestDto.getStationTotalDurationMinutes() / requestDto.getCoachingStations());
            }

            // Calculate number of full rotations possible
            int numberOfRotations = requestDto.getStationTotalDurationMinutes() /
                    (requestDto.getStationRotationMinutes() * requestDto.getCoachingStations());

            if (numberOfRotations < 1) {
                throw new IllegalArgumentException(
                        "The station rotation time (" + requestDto.getStationRotationMinutes() +
                                " min) is too long for the total station time (" +
                                requestDto.getStationTotalDurationMinutes() + " min) with " +
                                requestDto.getCoachingStations() + " stations");
            }
        }
    }

    /**
     * Create warmup section for the practice plan
     */
    private PracticePlanSection createWarmupSection(
            PracticePlanGenerateRequest requestDto,
            List<Drill> availableDrills,
            Set<Long> usedDrillIds) {

        int warmupDuration = requestDto.getWarmupDurationMinutes();

        // Find suitable warmup drills (prioritize fundamentals focus area)
        List<Drill> warmupDrills = availableDrills.stream()
                .filter(d -> d.getFocusAreaName().equalsIgnoreCase("Fundamentals"))
                .filter(d -> d.getDurationMinutes() != null && d.getDurationMinutes() <= warmupDuration)
                .sorted(Comparator.comparing(Drill::getDifficultyLevel))
                .collect(Collectors.toList());

        // If no fundamentals drills available, use any drill that fits time constraint
        if (warmupDrills.isEmpty()) {
            warmupDrills = availableDrills.stream()
                    .filter(d -> d.getDurationMinutes() != null && d.getDurationMinutes() <= warmupDuration)
                    .sorted(Comparator.comparing(Drill::getDifficultyLevel))
                    .collect(Collectors.toList());
        }

        if (!warmupDrills.isEmpty()) {
            Drill warmupDrill = selectRandomDrill(warmupDrills, usedDrillIds);
            if (warmupDrill != null) {
                usedDrillIds.add(warmupDrill.getId());

                PracticePlanSection warmupSection = new PracticePlanSection();
                warmupSection.setSectionType("Warmup");
                warmupSection.setDurationMinutes(warmupDuration);
                warmupSection.setDrill(warmupDrill);
                warmupSection.setCoachingPoints(generateCoachingPoints(warmupDrill));
                warmupSection.setConcurrent(false);

                return warmupSection;
            }
        }

        return null;
    }

    /**
     * Create concurrent stations section with rotations
     */
    private PracticePlanSection createStationsSection(
            PracticePlanGenerateRequest requestDto,
            List<Drill> availableDrills,
            Set<Long> usedDrillIds) {

        int totalStationTime = requestDto.getStationTotalDurationMinutes();
        int stationRotationTime = requestDto.getStationRotationMinutes();
        int numStations = requestDto.getCoachingStations();

        // Calculate how many full rotations we can have
        int numFullRotations = totalStationTime / (stationRotationTime * numStations);
        int remainingTime = totalStationTime % (stationRotationTime * numStations);
        int totalRotations = remainingTime > 0 ? numFullRotations + 1 : numFullRotations;

        // Create a section for stations with concurrent flag
        PracticePlanSection stationsSection = new PracticePlanSection();
        stationsSection.setSectionType("Stations");
        stationsSection.setDurationMinutes(totalStationTime);
        stationsSection.setConcurrent(true);
        stationsSection.setStationGroups(new ArrayList<>());

        // Create drills for each station
        List<Drill> stationDrills = new ArrayList<>();
        for (int i = 0; i < numStations; i++) {
            // Find suitable drills for this station
            List<Drill> stationDrillCandidates = availableDrills.stream()
                    .filter(d -> d.getDurationMinutes() != null && d.getDurationMinutes() <= stationRotationTime * 2) // Allow some flexibility
                    .filter(d -> !usedDrillIds.contains(d.getId())) // Avoid duplicate drills
                    .collect(Collectors.toList());

            if (stationDrillCandidates.isEmpty()) {
                // If we've used all available drills, allow reuse
                stationDrillCandidates = availableDrills.stream()
                        .filter(d -> d.getDurationMinutes() != null && d.getDurationMinutes() <= stationRotationTime * 2)
                        .collect(Collectors.toList());
            }

            if (!stationDrillCandidates.isEmpty()) {
                Drill stationDrill = selectBestDrill(stationDrillCandidates, null);
                usedDrillIds.add(stationDrill.getId());
                stationDrills.add(stationDrill);
            } else {
                // If we can't find a drill, just add a placeholder
                Drill placeholderDrill = new Drill();
                placeholderDrill.setName("Station " + (i + 1) + " Drill");
                placeholderDrill.setDescription("No suitable drill found");
                stationDrills.add(placeholderDrill);
            }
        }

        // Create rotation groups
        for (int rotation = 0; rotation < totalRotations; rotation++) {
            StationGroup rotationGroup = new StationGroup();
            rotationGroup.setRotationNumber(rotation + 1);

            // For the last rotation, we might have less time if there's a remainder
            if (rotation == numFullRotations && remainingTime > 0) {
                rotationGroup.setDurationMinutes(remainingTime);
            } else {
                rotationGroup.setDurationMinutes(stationRotationTime * numStations);
            }

            List<Station> stations = new ArrayList<>();

            // For each station in this rotation
            for (int stationIdx = 0; stationIdx < numStations; stationIdx++) {
                Station station = new Station();
                station.setStationNumber(stationIdx + 1);

                // Calculate which drill this station should use in this rotation
                // This creates the rotation pattern
                int drillIndex = (stationIdx + rotation) % numStations;
                station.setDrill(stationDrills.get(drillIndex));
                station.setCoachingPoints(
                        "Focus on proper technique. Group " + (stationIdx + 1) +
                                " at Station " + (stationIdx + 1) + " for Rotation " + (rotation + 1));

                stations.add(station);
            }

            rotationGroup.setStations(stations);
            stationsSection.getStationGroups().add(rotationGroup);
        }

        return stationsSection;
    }

    /**
     * Create position group section
     */
    private PracticePlanSection createPositionGroupSection(
            PracticePlanGenerateRequest requestDto,
            List<Drill> availableDrills,
            Set<Long> usedDrillIds) {

        int positionGroupDuration = requestDto.getPositionGroupDurationMinutes();

        // For position-specific drills, filter by the position ID if provided
        List<Drill> positionDrills = availableDrills;
        if (requestDto.getPositionId() != null) {
            Position position = positionService.getPositionById(requestDto.getPositionId());

            positionDrills = availableDrills.stream()
                    .filter(d -> d.getPositionIds().contains(position.getId()))
                    .filter(d -> d.getDurationMinutes() != null && d.getDurationMinutes() <= positionGroupDuration)
                    .filter(d -> !usedDrillIds.contains(d.getId()))
                    .collect(Collectors.toList());
        } else {
            positionDrills = availableDrills.stream()
                    .filter(d -> d.getDurationMinutes() != null && d.getDurationMinutes() <= positionGroupDuration)
                    .filter(d -> !usedDrillIds.contains(d.getId()))
                    .collect(Collectors.toList());
        }

        if (positionDrills.isEmpty()) {
            positionDrills = availableDrills.stream()
                    .filter(d -> d.getDurationMinutes() != null && d.getDurationMinutes() <= positionGroupDuration)
                    .collect(Collectors.toList());
        }

        if (!positionDrills.isEmpty()) {
            Drill positionDrill = selectRandomDrill(positionDrills, usedDrillIds);
            if (positionDrill != null) {
                usedDrillIds.add(positionDrill.getId());

                PracticePlanSection positionSection = new PracticePlanSection();
                positionSection.setSectionType("Position Group");
                positionSection.setDurationMinutes(positionGroupDuration);
                positionSection.setDrill(positionDrill);
                positionSection.setCoachingPoints("Focus on position-specific skills and techniques");
                positionSection.setConcurrent(false);

                return positionSection;
            }
        }

        return null;
    }

    /**
     * Create team time section
     */
    private PracticePlanSection createTeamTimeSection(
            PracticePlanGenerateRequest requestDto,
            List<Drill> availableDrills,
            Set<Long> usedDrillIds) {

        int teamTimeDuration = requestDto.getTeamTimeDurationMinutes();

        // Find suitable team drills (prioritize team-oriented drills)
        List<Drill> teamDrills = availableDrills.stream()
                .filter(d -> d.getDurationMinutes() != null && d.getDurationMinutes() <= teamTimeDuration)
                .filter(d -> !usedDrillIds.contains(d.getId()))
                // Prefer drills that involve team coordination for team time
                .sorted((d1, d2) -> {
                    boolean d1TeamFocused = d1.getDescription() != null &&
                            (d1.getDescription().toLowerCase().contains("team") ||
                                    d1.getDescription().toLowerCase().contains("group") ||
                                    d1.getDescription().toLowerCase().contains("scrimmage"));
                    boolean d2TeamFocused = d2.getDescription() != null &&
                            (d2.getDescription().toLowerCase().contains("team") ||
                                    d2.getDescription().toLowerCase().contains("group") ||
                                    d2.getDescription().toLowerCase().contains("scrimmage"));

                    if (d1TeamFocused && !d2TeamFocused) return -1;
                    if (!d1TeamFocused && d2TeamFocused) return 1;
                    return 0;
                })
                .collect(Collectors.toList());

        if (teamDrills.isEmpty()) {
            teamDrills = availableDrills.stream()
                    .filter(d -> d.getDurationMinutes() != null && d.getDurationMinutes() <= teamTimeDuration)
                    .collect(Collectors.toList());
        }

        if (!teamDrills.isEmpty()) {
            Drill teamDrill = selectRandomDrill(teamDrills, usedDrillIds);
            if (teamDrill != null) {
                usedDrillIds.add(teamDrill.getId());

                PracticePlanSection teamSection = new PracticePlanSection();
                teamSection.setSectionType("Team Time");
                teamSection.setDurationMinutes(teamTimeDuration);
                teamSection.setDrill(teamDrill);
                teamSection.setCoachingPoints("Focus on team coordination and game situations");
                teamSection.setConcurrent(false);

                return teamSection;
            }
        }

        return null;
    }

    /**
     * Calculate the total equipment needed for the practice
     */
    private List<PracticePlanEquipmentSummary> calculateEquipmentNeeded(List<PracticePlanSection> sections) {
        Map<Long, PracticePlanEquipmentSummary> equipmentMap = new HashMap<>();

        for (PracticePlanSection section : sections) {
            if (section.getConcurrent() && section.getStationGroups() != null) {
                // For concurrent stations, we need equipment for all stations at once
                for (StationGroup rotationGroup : section.getStationGroups()) {
                    for (Station station : rotationGroup.getStations()) {
                        addEquipmentFromDrill(equipmentMap, station.getDrill());
                    }
                    // Only count the first rotation since we reuse the same stations
                    break;
                }
            } else if (section.getDrill() != null) {
                // For regular sections just add the drill equipment
                addEquipmentFromDrill(equipmentMap, section.getDrill());
            }
        }

        return new ArrayList<>(equipmentMap.values());
    }

    /**
     * Helper to add equipment from a drill to the equipment map
     */
    private void addEquipmentFromDrill(
            Map<Long, PracticePlanEquipmentSummary> equipmentMap,
            Drill drill) {

        if (drill.getEquipment() != null) {
            for (DrillEquipment equipment : drill.getEquipment()) {
                if (equipmentMap.containsKey(equipment.getEquipmentId())) {
                    // Update quantity
                    PracticePlanEquipmentSummary summary = equipmentMap.get(equipment.getEquipmentId());
                    summary.setTotalQuantity(summary.getTotalQuantity() + equipment.getQuantity());
                } else {
                    // Add new entry
                    PracticePlanEquipmentSummary summary = new PracticePlanEquipmentSummary();
                    summary.setEquipmentId(equipment.getEquipmentId());
                    summary.setEquipmentName(equipment.getEquipmentName());
                    summary.setTotalQuantity(equipment.getQuantity());
                    equipmentMap.put(equipment.getEquipmentId(), summary);
                }
            }
        }
    }

    /**
     * Generate a description of the practice plan
     */
    private String generatePlanDescription(Sport sport, FocusArea focusArea, PracticePlanGenerateRequest request) {
        StringBuilder description = new StringBuilder();
        description.append("A ").append(request.getTotalDurationMinutes()).append("-minute ");
        description.append(sport.getName()).append(" practice plan");

        if (focusArea != null) {
            description.append(" focusing on ").append(focusArea.getName());
        }

        description.append(". Features a ").append(request.getWarmupDurationMinutes());
        description.append("-minute warmup");

        if (request.getStationTotalDurationMinutes() != null && request.getStationTotalDurationMinutes() > 0) {
            description.append(", ").append(request.getCoachingStations());
            description.append(" concurrent stations (").append(request.getStationRotationMinutes());
            description.append(" min rotations)");
        }

        if (request.getPositionGroupDurationMinutes() != null && request.getPositionGroupDurationMinutes() > 0) {
            description.append(", ").append(request.getPositionGroupDurationMinutes());
            description.append("-minute position group work");
        }

        if (request.getTeamTimeDurationMinutes() != null && request.getTeamTimeDurationMinutes() > 0) {
            description.append(", and a ").append(request.getTeamTimeDurationMinutes());
            description.append("-minute team activity");
        }

        description.append(".");

        return description.toString();
    }

}
