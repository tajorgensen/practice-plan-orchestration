package com.hbwj.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticePlanEquipmentSummary {

    private Long equipmentId;
    private String equipmentName;
    private Integer totalQuantity;
}
