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
public class Drill {

    private Long id;
    private String name;
    private String description;
    private String instructions;
    private Integer durationMinutes;
    private String difficultyLevel;
    private Long focusAreaId;
    private String focusAreaName; // For display purposes
    private Set<Long> sportIds = new HashSet<>();
    private Set<Long> positionIds = new HashSet<>();
    private Set<Long> kpiIds = new HashSet<>();
    private Set<DrillEquipment> equipment = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
