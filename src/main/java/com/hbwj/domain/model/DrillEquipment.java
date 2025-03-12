package com.hbwj.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrillEquipment {

    private Long id;
    private Long drillId;
    private Long equipmentId;
    private Integer quantity;
    private String drillName; // For display purposes
    private String equipmentName; // For display purposes
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
