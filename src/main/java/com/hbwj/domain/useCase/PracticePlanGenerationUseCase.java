package com.hbwj.domain.useCase;

import com.hbwj.adapter.model.PracticePlanGenerateRequest;
import com.hbwj.adapter.model.PracticePlanGenerateResponse;
import com.hbwj.adapter.model.PracticePlanSection;
import com.hbwj.domain.model.Drill;
import com.hbwj.domain.model.FocusArea;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Main use case for generating practice plans.
 * This class coordinates the overall process of creating a practice plan
 * but delegates specific section creation to specialized helpers.
 */
@AllArgsConstructor
@Component
public class PracticePlanGenerationUseCase {

    private final SportService sportService;
    private final FocusAreaService focusAreaService;
    private final DrillService drillService;
    private final DrillEquipmentService drillEquipmentService;
    private final PositionService positionService;

    // Helper instances - would be autowired in actual Spring application
    private final PracticePlanDrillSelector drillSelector;
    private final PracticePlanSectionGenerator sectionGenerator;
    private final PracticePlanValidator validator;
    private final PracticePlanEquipmentCalculator equipmentCalculator;

    /**
     * Main method to execute the practice plan generation process
     */
    public PracticePlanGenerateResponse execute(PracticePlanGenerateRequest requestDto) {
        // Get sport and focus area
        Sport sport = sportService.getSportById(requestDto.getSportId());

        FocusArea focusArea = null;
        if (requestDto.getFocusAreaId() != null) {
            focusArea = focusAreaService.getFocusAreaById(requestDto.getFocusAreaId());
        }

        // Validate all time parameters
        validator.validateTimeParameters(requestDto);

        // Find suitable drills
        List<Drill> availableDrills = drillSelector.findSuitableDrills(
                requestDto,
                drillService,
                positionService,
                drillEquipmentService
        );

        // Count how many drill slots we'll need to fill
        int estimatedDrillCount = calculateRequiredDrillCount(requestDto);

        if (availableDrills.size() < estimatedDrillCount) {
            // Log a warning that we might not have enough unique drills
            System.out.println("WARNING: Found " + availableDrills.size() +
                    " drills but might need approximately " + estimatedDrillCount +
                    " for the practice plan. Generic drills may be used.");
        }

        // Check that we have at least some drills to work with
        if (availableDrills.isEmpty()) {
            throw new ResourceNotFoundException("No suitable drills found for the specified criteria");
        }

        // Create the response object
        PracticePlanGenerateResponse responseDto = new PracticePlanGenerateResponse();
        responseDto.setName(generatePlanName(sport, focusArea));
        responseDto.setSportName(sport.getName());
        responseDto.setFocusAreaName(focusArea != null ? focusArea.getName() : "Mixed");

        // Generate the practice plan sections
        List<PracticePlanSection> sections = new ArrayList<>();
        Set<Long> usedDrillIds = new HashSet<>();
        int allocatedTime = 0;

        // 1. Add warmup section
        PracticePlanSection warmupSection = sectionGenerator.createWarmupSection(
                requestDto, availableDrills, usedDrillIds, drillSelector);
        if (warmupSection != null) {
            sections.add(warmupSection);
            allocatedTime += warmupSection.getDurationMinutes();
        }

        // 2. Add station sections with rotations if applicable
        boolean hasStations = false;
        if (requestDto.getStationTotalDurationMinutes() != null && requestDto.getStationTotalDurationMinutes() > 0) {
            PracticePlanSection stationsSection = sectionGenerator.createStationsSection(
                    requestDto, availableDrills, usedDrillIds, drillSelector);
            if (stationsSection != null) {
                sections.add(stationsSection);
                allocatedTime += stationsSection.getDurationMinutes();
                hasStations = true;
            }
        }

        // 3. Add water break after stations or at 45-minute mark
        if (hasStations || allocatedTime >= 45) {
            PracticePlanSection waterBreak = sectionGenerator.createWaterBreakSection();
            sections.add(waterBreak);
            allocatedTime += waterBreak.getDurationMinutes();
        }

        // 4. Add position group sections based on focus area
        if (requestDto.getPositionGroupDurationMinutes() != null && requestDto.getPositionGroupDurationMinutes() > 0) {
            // If no specific focus area, create both offensive and defensive position groups
            if (requestDto.getFocusAreaId() == null) {
                // Add offensive position group section
                PracticePlanSection offensivePositionSection = sectionGenerator.createFocusAreaPositionGroupSection(
                        requestDto, availableDrills, usedDrillIds, "Offense",
                        focusAreaService, positionService, drillSelector);
                if (offensivePositionSection != null) {
                    sections.add(offensivePositionSection);
                    allocatedTime += offensivePositionSection.getDurationMinutes();
                }

                // Add defensive position group section
                PracticePlanSection defensivePositionSection = sectionGenerator.createFocusAreaPositionGroupSection(
                        requestDto, availableDrills, usedDrillIds, "Defense",
                        focusAreaService, positionService, drillSelector);
                if (defensivePositionSection != null) {
                    sections.add(defensivePositionSection);
                    allocatedTime += defensivePositionSection.getDurationMinutes();
                }
            } else if (focusArea != null) {
                // Only create position group section for the selected focus area
                if (focusArea.getName().equalsIgnoreCase("Offense") ||
                        focusArea.getName().equalsIgnoreCase("Defense")) {
                    PracticePlanSection positionGroupSection = sectionGenerator.createPositionGroupSection(
                            requestDto, availableDrills, usedDrillIds, focusArea,
                            positionService, drillSelector);
                    if (positionGroupSection != null) {
                        sections.add(positionGroupSection);
                        allocatedTime += positionGroupSection.getDurationMinutes();
                    }
                }
            }
        }

        // 5. Calculate remaining time for team time section
        int remainingTime = requestDto.getTotalDurationMinutes() - allocatedTime;

        // Only add team time if there's meaningful time left (at least 10 minutes)
        if (remainingTime >= 10) {
            // Create team time section with remaining time
            requestDto.setTeamTimeDurationMinutes(remainingTime);
            PracticePlanSection teamTimeSection = sectionGenerator.createTeamTimeSection(
                    requestDto, availableDrills, usedDrillIds, drillSelector);
            if (teamTimeSection != null) {
                sections.add(teamTimeSection);
                allocatedTime += teamTimeSection.getDurationMinutes();
            }
        }

        // Add additional water breaks at 45-minute intervals as needed
        sections = sectionGenerator.insertAdditionalWaterBreaks(sections);

        // Calculate total duration including water breaks
        int totalDuration = 0;
        for (PracticePlanSection section : sections) {
            totalDuration += section.getDurationMinutes();
        }

        responseDto.setTotalDurationMinutes(totalDuration);
        responseDto.setSections(sections);

        // Generate description after all sections are created
        responseDto.setDescription(generatePlanDescription(sport, focusArea, requestDto, hasStations));

        // Calculate equipment needed
        responseDto.setEquipmentNeeded(equipmentCalculator.calculateEquipmentNeeded(sections));

        return responseDto;
    }

    // Add this helper method to estimate how many unique drills we'll need
    private int calculateRequiredDrillCount(PracticePlanGenerateRequest requestDto) {
        int count = 0;

        // Warmup (1 drill)
        count += 1;

        // Stations
        if (requestDto.getStationTotalDurationMinutes() != null && requestDto.getStationTotalDurationMinutes() > 0) {
            count += requestDto.getCoachingStations();
        }

        // Position groups (approximately 1 per position)
        if (requestDto.getPositionGroupDurationMinutes() != null && requestDto.getPositionGroupDurationMinutes() > 0) {
            // Rough estimate - about 5 positions per sport on average
            count += 5;

            // If no specific focus area, we'll have both offense and defense position groups
            if (requestDto.getFocusAreaId() == null) {
                count += 5; // Double for both offense and defense
            }
        }

        // Team time (1 drill)
        count += 1;

        return count;
    }

    /**
     * Generate a unique name for the practice plan
     */
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

    /**
     * Generate a description of the practice plan structure
     */
    private String generatePlanDescription(Sport sport, FocusArea focusArea,
                                           PracticePlanGenerateRequest request,
                                           boolean hasStations) {
        StringBuilder description = new StringBuilder();
        description.append("A ").append(request.getTotalDurationMinutes()).append("-minute ");
        description.append(sport.getName()).append(" practice plan");

        if (focusArea != null) {
            description.append(" focusing on ").append(focusArea.getName());
        }

        description.append(". Structure: ").append(request.getWarmupDurationMinutes());
        description.append("-minute warmup");

        if (hasStations) {
            description.append(" → ").append(request.getCoachingStations());
            description.append(" station rotations (").append(Math.max(5, request.getStationRotationMinutes()));
            description.append(" min each)");

            // Add water break after stations
            description.append(" → water break");
        } else {
            // Add water break after 45 minutes if no stations
            description.append(" → water break after 45 minutes");
        }

        // Add position group sections to description
        if (request.getPositionGroupDurationMinutes() != null && request.getPositionGroupDurationMinutes() > 0) {
            if (focusArea == null) {
                description.append(" → ").append(request.getPositionGroupDurationMinutes());
                description.append("-minute offensive position groups followed by ");
                description.append(request.getPositionGroupDurationMinutes());
                description.append("-minute defensive position groups");
            } else if (focusArea.getName().equalsIgnoreCase("Offense") ||
                    focusArea.getName().equalsIgnoreCase("Defense")) {
                description.append(" → ").append(request.getPositionGroupDurationMinutes());
                description.append("-minute ").append(focusArea.getName()).append(" position groups");
            }
        }

        // Add team time to description
        description.append(" → team time to complete the practice");

        // Add additional info
        description.append(". Water breaks occur every 45 minutes to ensure player hydration. ");
        description.append("During position group time, each position works together on position-specific skills and drills.");

        return description.toString();
    }
}