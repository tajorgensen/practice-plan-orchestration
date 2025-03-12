package com.hbwj.adapter.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticePlanGenerateRequest {

    @NotNull(message = "Sport ID is required")
    private Long sportId;

    private Long focusAreaId; // Optional - if null, will include drills from all focus areas

    @Min(value = 30, message = "Total practice duration must be at least 30 minutes")
    @Max(value = 240, message = "Total practice duration must not exceed 240 minutes")
    private Integer totalDurationMinutes;

    @Min(value = 5, message = "Warmup duration must be at least 5 minutes")
    @Max(value = 30, message = "Warmup duration must not exceed 30 minutes")
    private Integer warmupDurationMinutes;

    // Station parameters
    @Min(value = 1, message = "Number of coaching stations must be at least 1")
    @Max(value = 6, message = "Number of coaching stations must not exceed 6")
    private Integer coachingStations;

    @Min(value = 0, message = "Station duration must be non-negative")
    @Max(value = 60, message = "Station duration must not exceed 60 minutes")
    private Integer stationTotalDurationMinutes; // Total time for all station rotations

    @Min(value = 0, message = "Rotation duration must be non-negative")
    @Max(value = 20, message = "Rotation duration must not exceed 20 minutes")
    private Integer stationRotationMinutes; // Time spent at each station before rotation

    // Optional position group time
    private Integer positionGroupDurationMinutes;

    // Optional team time
    private Integer teamTimeDurationMinutes;

    // Optional parameters for more detailed customization
    private Long positionId; // If specified, include drills for this position
    private String ageGroup; // e.g., "6-8", "9-11", "12-14", "15-17", "18+"
    private Integer maxEquipmentTypes; // Limit the variety of equipment needed
}

