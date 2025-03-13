package com.hbwj.adapter.model;

import com.hbwj.domain.model.Drill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Station {
    private Integer stationNumber; // Station 1, 2, 3, etc.
    private String stationName; // "Station 1" or position name like "Quarterback"
    private Drill drill;
    private String coachingPoints;
}