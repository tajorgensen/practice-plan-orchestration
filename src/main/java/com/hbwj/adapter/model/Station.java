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
    private Drill drill;
    private String coachingPoints;
}
