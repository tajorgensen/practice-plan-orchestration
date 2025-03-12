package com.hbwj.domain.useCase;

import com.hbwj.adapter.model.PracticePlanSection;
import com.hbwj.adapter.model.Station;
import com.hbwj.adapter.model.StationGroup;
import com.hbwj.domain.model.Drill;
import com.hbwj.domain.model.DrillEquipment;
import com.hbwj.domain.model.PracticePlanEquipmentSummary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for calculating equipment needs for practice plans
 */
@Component
public class PracticePlanEquipmentCalculator {

    /**
     * Calculate the total equipment needed for the practice
     */
    public List<PracticePlanEquipmentSummary> calculateEquipmentNeeded(List<PracticePlanSection> sections) {
        Map<Long, PracticePlanEquipmentSummary> equipmentMap = new HashMap<>();

        for (PracticePlanSection section : sections) {
            if (section.getConcurrent() && section.getStationGroups() != null) {
                // For concurrent stations, we need equipment for all stations at once
                for (StationGroup rotationGroup : section.getStationGroups()) {
                    for (Station station : rotationGroup.getStations()) {
                        addEquipmentFromDrill(equipmentMap, station.getDrill());
                    }
                    // Only count the first rotation since we reuse the same stations
                    break;
                }
            } else if (section.getDrill() != null) {
                // For regular sections just add the drill equipment
                addEquipmentFromDrill(equipmentMap, section.getDrill());
            }
        }

        return new ArrayList<>(equipmentMap.values());
    }

    /**
     * Helper to add equipment from a drill to the equipment map
     */
    private void addEquipmentFromDrill(
            Map<Long, PracticePlanEquipmentSummary> equipmentMap,
            Drill drill) {

        if (drill.getEquipment() != null) {
            for (DrillEquipment equipment : drill.getEquipment()) {
                if (equipmentMap.containsKey(equipment.getEquipmentId())) {
                    // Update quantity
                    PracticePlanEquipmentSummary summary = equipmentMap.get(equipment.getEquipmentId());
                    summary.setTotalQuantity(summary.getTotalQuantity() + equipment.getQuantity());
                } else {
                    // Add new entry
                    PracticePlanEquipmentSummary summary = new PracticePlanEquipmentSummary();
                    summary.setEquipmentId(equipment.getEquipmentId());
                    summary.setEquipmentName(equipment.getEquipmentName());
                    summary.setTotalQuantity(equipment.getQuantity());
                    equipmentMap.put(equipment.getEquipmentId(), summary);
                }
            }
        }
    }
}