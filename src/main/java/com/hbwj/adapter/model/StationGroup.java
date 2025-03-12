package com.hbwj.adapter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationGroup {
    private Integer rotationNumber; // Which rotation this is (1, 2, 3, etc.)
    private Integer durationMinutes; // Duration of this rotation
    private List<Station> stations; // The stations in this rotation
}
