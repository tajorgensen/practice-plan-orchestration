package com.hbwj.adapter.gateway.service;

import com.hbwj.adapter.gateway.repository.EquipmentRepository;
import com.hbwj.adapter.gateway.repository.entity.EquipmentEntity;
import com.hbwj.domain.model.Equipment;
import com.hbwj.domain.port.EquipmentService;
import com.hbwj.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Equipment getEquipmentById(Long id) {
        EquipmentEntity equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
        return mapToDto(equipment);
    }

    @Override
    @Transactional
    public Equipment createEquipment(Equipment domain) {
        EquipmentEntity equipment = new EquipmentEntity();
        equipment.setName(domain.getName());
        equipment.setDescription(domain.getDescription());

        EquipmentEntity savedEquipment = equipmentRepository.save(equipment);
        return mapToDto(savedEquipment);
    }

    @Override
    @Transactional
    public Equipment updateEquipment(Long id, Equipment domain) {
        EquipmentEntity equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));

        equipment.setName(domain.getName());
        equipment.setDescription(domain.getDescription());

        EquipmentEntity updatedEquipment = equipmentRepository.save(equipment);
        return mapToDto(updatedEquipment);
    }

    @Override
    @Transactional
    public void deleteEquipment(Long id) {
        if (!equipmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Equipment not found with id: " + id);
        }
        equipmentRepository.deleteById(id);
    }

    private Equipment mapToDto(EquipmentEntity equipment) {
        Equipment domain = new Equipment();
        domain.setId(equipment.getId());
        domain.setName(equipment.getName());
        domain.setDescription(equipment.getDescription());
        domain.setCreatedAt(equipment.getCreatedAt());
        domain.setUpdatedAt(equipment.getUpdatedAt());
        return domain;
    }
}

