package com.hbwj.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrillKpi {

    private Long id;
    private Long drillId;
    private Long kpiId;
    private Integer impactLevel;
    private String drillName; // For display purposes
    private String kpiName; // For display purposes
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
