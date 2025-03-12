package com.hbwj.adapter.model;

import com.hbwj.domain.model.Drill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticePlanSection {
    private String sectionType; // "Warmup", "Stations", "Position Group", "Team Time"
    private Integer durationMinutes;
    private Drill drill; // For warmup, position group, or team time sections
    private List<StationGroup> stationGroups; // For station sections
    private String coachingPoints; // Key points for coaches to emphasize
    private Boolean concurrent;
}
