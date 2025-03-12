package com.hbwj.adapter.gateway.service;

import com.hbwj.adapter.gateway.repository.DrillEquipmentRepository;
import com.hbwj.adapter.gateway.repository.DrillRepository;
import com.hbwj.adapter.gateway.repository.EquipmentRepository;
import com.hbwj.adapter.gateway.repository.entity.DrillEntity;
import com.hbwj.adapter.gateway.repository.entity.DrillEquipmentEntity;
import com.hbwj.adapter.gateway.repository.entity.EquipmentEntity;
import com.hbwj.domain.model.DrillEquipment;
import com.hbwj.domain.port.DrillEquipmentService;
import com.hbwj.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DrillEquipmentServiceImpl implements DrillEquipmentService {

    private final DrillEquipmentRepository drillEquipmentRepository;
    private final DrillRepository drillRepository;
    private final EquipmentRepository equipmentRepository;

    @Autowired
    public DrillEquipmentServiceImpl(
            DrillEquipmentRepository drillEquipmentRepository,
            DrillRepository drillRepository,
            EquipmentRepository equipmentRepository) {
        this.drillEquipmentRepository = drillEquipmentRepository;
        this.drillRepository = drillRepository;
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public List<DrillEquipment> getAllDrillEquipment() {
        return drillEquipmentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DrillEquipment> getDrillEquipmentByDrillId(Long drillId) {
        return drillEquipmentRepository.findByDrillId(drillId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DrillEquipment> getDrillEquipmentByEquipmentId(Long equipmentId) {
        return drillEquipmentRepository.findByEquipmentId(equipmentId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DrillEquipment getDrillEquipmentById(Long id) {
        DrillEquipmentEntity drillEquipment = drillEquipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drill Equipment not found with id: " + id));
        return mapToDto(drillEquipment);
    }

    @Override
    @Transactional
    public DrillEquipment createDrillEquipment(DrillEquipment domain) {
        DrillEntity drill = drillRepository.findById(domain.getDrillId())
                .orElseThrow(() -> new ResourceNotFoundException("Drill not found with id: " + domain.getDrillId()));

        EquipmentEntity equipment = equipmentRepository.findById(domain.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + domain.getEquipmentId()));

        // Check if there's already a relationship
        Optional<DrillEquipmentEntity> existingRelation = drillEquipmentRepository.findAll().stream()
                .filter(de -> de.getDrill().getId().equals(drill.getId()) &&
                        de.getEquipment().getId().equals(equipment.getId()))
                .findFirst();

        if (existingRelation.isPresent()) {
            // Update existing relation
            DrillEquipmentEntity drillEquipment = existingRelation.get();
            drillEquipment.setQuantity(drillEquipment.getQuantity());
            return mapToDto(drillEquipmentRepository.save(drillEquipment));
        }

        // Create new relation
        DrillEquipmentEntity drillEquipment = new DrillEquipmentEntity();
        drillEquipment.setDrill(drill);
        drillEquipment.setEquipment(equipment);
        drillEquipment.setQuantity(drillEquipment.getQuantity());

        DrillEquipmentEntity savedDrillEquipment = drillEquipmentRepository.save(drillEquipment);
        return mapToDto(savedDrillEquipment);
    }

    @Override
    @Transactional
    public DrillEquipment updateDrillEquipment(Long id, DrillEquipment domain) {
        DrillEquipmentEntity drillEquipment = drillEquipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drill Equipment not found with id: " + id));

        DrillEntity drill = drillRepository.findById(domain.getDrillId())
                .orElseThrow(() -> new ResourceNotFoundException("Drill not found with id: " + domain.getDrillId()));

        EquipmentEntity equipment = equipmentRepository.findById(domain.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + domain.getEquipmentId()));

        drillEquipment.setDrill(drill);
        drillEquipment.setEquipment(equipment);
        drillEquipment.setQuantity(drillEquipment.getQuantity());

        DrillEquipmentEntity updatedDrillEquipment = drillEquipmentRepository.save(drillEquipment);
        return mapToDto(updatedDrillEquipment);
    }

    @Override
    @Transactional
    public void deleteDrillEquipment(Long id) {
        if (!drillEquipmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Drill Equipment not found with id: " + id);
        }
        drillEquipmentRepository.deleteById(id);
    }

    private DrillEquipment mapToDto(DrillEquipmentEntity drillEquipment) {
        DrillEquipment domain = new DrillEquipment();
        domain.setId(drillEquipment.getId());
        domain.setDrillId(drillEquipment.getDrill().getId());
        domain.setEquipmentId(drillEquipment.getEquipment().getId());
        domain.setQuantity(drillEquipment.getQuantity());
        domain.setDrillName(drillEquipment.getDrill().getName());
        domain.setEquipmentName(drillEquipment.getEquipment().getName());
        domain.setCreatedAt(drillEquipment.getCreatedAt());
        domain.setUpdatedAt(drillEquipment.getUpdatedAt());
        return domain;
    }
}
