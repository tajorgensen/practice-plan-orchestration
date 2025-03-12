package com.hbwj.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticePlan {

    private Long id;
    private String name;
    private String description;
    private Long sportId;
    private Long focusAreaId;
    private Integer totalDurationMinutes;
    private String sportName; // For display purposes
    private String focusAreaName; // For display purposes
    private Set<PracticePlanDrill> drills = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
