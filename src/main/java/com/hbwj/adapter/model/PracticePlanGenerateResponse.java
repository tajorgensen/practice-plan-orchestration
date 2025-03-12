package com.hbwj.adapter.model;

import com.hbwj.domain.model.PracticePlanEquipmentSummary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticePlanGenerateResponse {
    private Long id; // ID of the generated practice plan
    private String name;
    private String description;
    private String sportName;
    private String focusAreaName;
    private Integer totalDurationMinutes;
    private List<PracticePlanSection> sections;
    private List<PracticePlanEquipmentSummary> equipmentNeeded;
}
