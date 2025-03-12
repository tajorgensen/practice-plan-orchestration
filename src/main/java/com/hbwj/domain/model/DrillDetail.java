package com.hbwj.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrillDetail extends Drill {

    private Set<Sport> sports = new HashSet<>();
    private Set<Position> positions = new HashSet<>();
    private Set<Kpi> kpis = new HashSet<>();
    private Integer impactLevel; // For drill-kpi relationship
}
