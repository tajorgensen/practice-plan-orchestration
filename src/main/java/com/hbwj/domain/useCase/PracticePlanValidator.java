package com.hbwj.domain.useCase;

import com.hbwj.adapter.model.PracticePlanGenerateRequest;
import org.springframework.stereotype.Component;

/**
 * Helper class for validating practice plan parameters
 */
@Component
public class PracticePlanValidator {

    /**
     * Validate all time parameters to ensure they're consistent and feasible
     */
    public void validateTimeParameters(PracticePlanGenerateRequest requestDto) {
        int allocatedTime = requestDto.getWarmupDurationMinutes();

        // Add station time if specified
        if (requestDto.getStationTotalDurationMinutes() != null) {
            allocatedTime += requestDto.getStationTotalDurationMinutes();
        }

        // Add position group time if specified (doubled if all focus areas)
        if (requestDto.getPositionGroupDurationMinutes() != null) {
            // If no specific focus area, we'll have both offense and defense sections
            if (requestDto.getFocusAreaId() == null) {
                allocatedTime += requestDto.getPositionGroupDurationMinutes() * 2;
            } else {
                allocatedTime += requestDto.getPositionGroupDurationMinutes();
            }
        }

        // Add time for water breaks (approximate)
        int practiceTimeWithoutBreaks = allocatedTime;
        int estimatedBreaks = practiceTimeWithoutBreaks / 45;
        if (practiceTimeWithoutBreaks % 45 > 0) {
            estimatedBreaks += 1;
        }
        allocatedTime += estimatedBreaks * 5;

        // Check if we have enough time for everything including breaks
        if (allocatedTime > requestDto.getTotalDurationMinutes()) {
            throw new IllegalArgumentException(
                    "The sum of warmup, station, position group durations, and required water breaks (" +
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
}