package com.hbwj.domain.port;

import com.hbwj.domain.model.Equipment;

import java.util.List;

public interface EquipmentService {
    List<Equipment> getAllEquipment();

    Equipment getEquipmentById(Long id);

    Equipment createEquipment(Equipment equipment);

    Equipment updateEquipment(Long id, Equipment equipment);

    void deleteEquipment(Long id);
}
