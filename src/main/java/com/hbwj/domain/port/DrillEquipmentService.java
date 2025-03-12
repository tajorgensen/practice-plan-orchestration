package com.hbwj.domain.port;

import com.hbwj.domain.model.DrillEquipment;

import java.util.List;

public interface DrillEquipmentService {
    List<DrillEquipment> getAllDrillEquipment();

    List<DrillEquipment> getDrillEquipmentByDrillId(Long drillId);

    List<DrillEquipment> getDrillEquipmentByEquipmentId(Long equipmentId);

    DrillEquipment getDrillEquipmentById(Long id);

    DrillEquipment createDrillEquipment(DrillEquipment drillEquipment);

    DrillEquipment updateDrillEquipment(Long id, DrillEquipment drillEquipment);

    void deleteDrillEquipment(Long id);
}
