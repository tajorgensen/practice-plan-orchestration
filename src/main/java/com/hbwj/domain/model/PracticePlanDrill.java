package com.hbwj.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticePlanDrill {

    private Long id;
    private Long practicePlanId;
    private Long drillId;
    private Integer sequenceOrder;
    private Integer durationMinutes;
    private String notes;
    private Drill drill; // Include the drill details
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
