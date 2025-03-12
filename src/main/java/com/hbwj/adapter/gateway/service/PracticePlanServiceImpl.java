package com.hbwj.adapter.gateway.service;

import com.hbwj.adapter.gateway.repository.DrillEquipmentRepository;
import com.hbwj.adapter.gateway.repository.DrillRepository;
import com.hbwj.adapter.gateway.repository.EquipmentRepository;
import com.hbwj.adapter.gateway.repository.FocusAreaRepository;
import com.hbwj.adapter.gateway.repository.PracticePlanDrillRepository;
import com.hbwj.adapter.gateway.repository.PracticePlanRepository;
import com.hbwj.adapter.gateway.repository.SportRepository;
import com.hbwj.adapter.gateway.repository.entity.DrillEntity;
import com.hbwj.adapter.gateway.repository.entity.DrillEquipmentEntity;
import com.hbwj.adapter.gateway.repository.entity.EquipmentEntity;
import com.hbwj.adapter.gateway.repository.entity.FocusAreaEntity;
import com.hbwj.adapter.gateway.repository.entity.PracticePlanDrillEntity;
import com.hbwj.adapter.gateway.repository.entity.PracticePlanEntity;
import com.hbwj.adapter.gateway.repository.entity.SportEntity;
import com.hbwj.domain.model.Drill;
import com.hbwj.domain.model.PracticePlan;
import com.hbwj.domain.model.PracticePlanDrill;
import com.hbwj.domain.model.PracticePlanEquipmentSummary;
import com.hbwj.domain.port.PracticePlanService;
import com.hbwj.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PracticePlanServiceImpl implements PracticePlanService {

    private final PracticePlanRepository practicePlanRepository;
    private final PracticePlanDrillRepository practicePlanDrillRepository;
    private final SportRepository sportRepository;
    private final FocusAreaRepository focusAreaRepository;
    private final DrillRepository drillRepository;
    private final DrillEquipmentRepository drillEquipmentRepository;
    private final EquipmentRepository equipmentRepository;

    @Autowired
    public PracticePlanServiceImpl(
            PracticePlanRepository practicePlanRepository,
            PracticePlanDrillRepository practicePlanDrillRepository,
            SportRepository sportRepository,
            FocusAreaRepository focusAreaRepository,
            DrillRepository drillRepository,
            DrillEquipmentRepository drillEquipmentRepository,
            EquipmentRepository equipmentRepository) {
        this.practicePlanRepository = practicePlanRepository;
        this.practicePlanDrillRepository = practicePlanDrillRepository;
        this.sportRepository = sportRepository;
        this.focusAreaRepository = focusAreaRepository;
        this.drillRepository = drillRepository;
        this.drillEquipmentRepository = drillEquipmentRepository;
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public List<PracticePlan> getAllPracticePlans() {
        return practicePlanRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PracticePlan> getPracticePlansBySportId(Long sportId) {
        return practicePlanRepository.findBySportId(sportId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PracticePlan> getPracticePlansByFocusAreaId(Long focusAreaId) {
        return practicePlanRepository.findByFocusAreaId(focusAreaId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PracticePlan> getPracticePlansBySportIdAndFocusAreaId(Long sportId, Long focusAreaId) {
        return practicePlanRepository.findBySportIdAndFocusAreaId(sportId, focusAreaId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PracticePlan getPracticePlanById(Long id) {
        PracticePlanEntity practicePlan = practicePlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Practice Plan not found with id: " + id));
        return mapToDto(practicePlan);
    }

    @Override
    @Transactional
    public PracticePlan createPracticePlan(PracticePlan domain) {
        SportEntity sport = sportRepository.findById(domain.getSportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport not found with id: " + domain.getSportId()));

        // Focus area is optional
        FocusAreaEntity focusArea = null;
        if (domain.getFocusAreaId() != null) {
            focusArea = focusAreaRepository.findById(domain.getFocusAreaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Focus Area not found with id: " + domain.getFocusAreaId()));
        }

        PracticePlanEntity practicePlan = new PracticePlanEntity();
        practicePlan.setName(domain.getName());
        practicePlan.setDescription(domain.getDescription());
        practicePlan.setSport(sport);
        practicePlan.setFocusArea(focusArea);
        practicePlan.setTotalDurationMinutes(domain.getTotalDurationMinutes());

        PracticePlanEntity savedPracticePlanEntity = practicePlanRepository.save(practicePlan);

        // Handle drills if provided
        if (domain.getDrills() != null && !domain.getDrills().isEmpty()) {
            for (PracticePlanDrill drillDto : domain.getDrills()) {
                DrillEntity drill = drillRepository.findById(drillDto.getDrillId())
                        .orElseThrow(() -> new ResourceNotFoundException("Drill not found with id: " + drillDto.getDrillId()));

                PracticePlanDrillEntity practicePlanDrill = new PracticePlanDrillEntity();
                practicePlanDrill.setPracticePlan(savedPracticePlanEntity);
                practicePlanDrill.setDrill(drill);
                practicePlanDrill.setSequenceOrder(drillDto.getSequenceOrder());
                practicePlanDrill.setDurationMinutes(drillDto.getDurationMinutes());
                practicePlanDrill.setNotes(drillDto.getNotes());

                practicePlanDrillRepository.save(practicePlanDrill);
            }
        }

        // Recalculate total duration
        updateTotalDuration(savedPracticePlanEntity.getId());

        return mapToDto(practicePlanRepository.findById(savedPracticePlanEntity.getId()).get());
    }

    @Override
    @Transactional
    public PracticePlan updatePracticePlan(Long id, PracticePlan practicePlanDto) {
        PracticePlanEntity practicePlan = practicePlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Practice Plan not found with id: " + id));

        SportEntity sport = sportRepository.findById(practicePlanDto.getSportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport not found with id: " + practicePlanDto.getSportId()));

        // Focus area is optional
        FocusAreaEntity focusArea = null;
        if (practicePlanDto.getFocusAreaId() != null) {
            focusArea = focusAreaRepository.findById(practicePlanDto.getFocusAreaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Focus Area not found with id: " + practicePlanDto.getFocusAreaId()));
        }

        practicePlan.setName(practicePlanDto.getName());
        practicePlan.setDescription(practicePlanDto.getDescription());
        practicePlan.setSport(sport);
        practicePlan.setFocusArea(focusArea);
        practicePlan.setTotalDurationMinutes(practicePlanDto.getTotalDurationMinutes());

        PracticePlanEntity updatedPracticePlanEntity = practicePlanRepository.save(practicePlan);

        // Handle drills update if provided
        if (practicePlanDto.getDrills() != null) {
            // Get current drill associations
            List<PracticePlanDrillEntity> currentDrills = practicePlanDrillRepository.findByPracticePlanId(id);

            // Create a map of sequence order to PracticePlanDrillEntity for easy lookup
            Map<Integer, PracticePlanDrillEntity> currentDrillsMap = currentDrills.stream()
                    .collect(Collectors.toMap(
                            PracticePlanDrillEntity::getSequenceOrder,
                            drill -> drill
                    ));

            // Create a set of IDs for drills that should be kept
            Set<Long> drillsToKeep = new HashSet<>();

            // Process each drill in the DTO
            for (PracticePlanDrill drillDto : practicePlanDto.getDrills()) {
                if (drillDto.getId() != null && currentDrills.stream().anyMatch(cd -> cd.getId().equals(drillDto.getId()))) {
                    // Update existing drill association
                    PracticePlanDrillEntity practicePlanDrill = currentDrills.stream()
                            .filter(cd -> cd.getId().equals(drillDto.getId()))
                            .findFirst().get();

                    DrillEntity drill = drillRepository.findById(drillDto.getDrillId())
                            .orElseThrow(() -> new ResourceNotFoundException("Drill not found with id: " + drillDto.getDrillId()));

                    practicePlanDrill.setDrill(drill);
                    practicePlanDrill.setSequenceOrder(drillDto.getSequenceOrder());
                    practicePlanDrill.setDurationMinutes(drillDto.getDurationMinutes());
                    practicePlanDrill.setNotes(drillDto.getNotes());

                    practicePlanDrillRepository.save(practicePlanDrill);
                    drillsToKeep.add(practicePlanDrill.getId());
                } else {
                    // Add new drill association
                    DrillEntity drill = drillRepository.findById(drillDto.getDrillId())
                            .orElseThrow(() -> new ResourceNotFoundException("Drill not found with id: " + drillDto.getDrillId()));

                    PracticePlanDrillEntity practicePlanDrill = new PracticePlanDrillEntity();
                    practicePlanDrill.setPracticePlan(updatedPracticePlanEntity);
                    practicePlanDrill.setDrill(drill);
                    practicePlanDrill.setSequenceOrder(drillDto.getSequenceOrder());
                    practicePlanDrill.setDurationMinutes(drillDto.getDurationMinutes());
                    practicePlanDrill.setNotes(drillDto.getNotes());

                    PracticePlanDrillEntity saved = practicePlanDrillRepository.save(practicePlanDrill);
                    drillsToKeep.add(saved.getId());
                }
            }

            // Remove any drill associations that were not in the update
            List<PracticePlanDrillEntity> drillsToRemove = currentDrills.stream()
                    .filter(cd -> !drillsToKeep.contains(cd.getId()))
                    .collect(Collectors.toList());

            practicePlanDrillRepository.deleteAll(drillsToRemove);
        }

        // Recalculate total duration
        updateTotalDuration(updatedPracticePlanEntity.getId());

        return mapToDto(practicePlanRepository.findById(updatedPracticePlanEntity.getId()).get());
    }

    @Override
    @Transactional
    public void deletePracticePlan(Long id) {
        if (!practicePlanRepository.existsById(id)) {
            throw new ResourceNotFoundException("Practice Plan not found with id: " + id);
        }

        // Delete related practice plan drill records first
        List<PracticePlanDrillEntity> practicePlanDrills = practicePlanDrillRepository.findByPracticePlanId(id);
        practicePlanDrillRepository.deleteAll(practicePlanDrills);

        // Now delete the practice plan
        practicePlanRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addDrillToPracticePlan(Long practicePlanId, PracticePlanDrill domain) {
        PracticePlanEntity practicePlan = practicePlanRepository.findById(practicePlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Practice Plan not found with id: " + practicePlanId));

        DrillEntity drill = drillRepository.findById(domain.getDrillId())
                .orElseThrow(() -> new ResourceNotFoundException("Drill not found with id: " + domain.getDrillId()));

        // Check if there's already a drill at this sequence order
        Optional<PracticePlanDrillEntity> existingDrillAtSequence = practicePlanDrillRepository.findByPracticePlanId(practicePlanId).stream()
                .filter(ppd -> ppd.getSequenceOrder().equals(domain.getSequenceOrder()))
                .findFirst();

        if (existingDrillAtSequence.isPresent()) {
            // Shift all drills after this sequence order
            List<PracticePlanDrillEntity> drillsToShift = practicePlanDrillRepository.findByPracticePlanId(practicePlanId).stream()
                    .filter(ppd -> ppd.getSequenceOrder() >= domain.getSequenceOrder())
                    .sorted(Comparator.comparing(PracticePlanDrillEntity::getSequenceOrder).reversed())
                    .collect(Collectors.toList());

            for (PracticePlanDrillEntity drillToShift : drillsToShift) {
                drillToShift.setSequenceOrder(drillToShift.getSequenceOrder() + 1);
                practicePlanDrillRepository.save(drillToShift);
            }
        }

        // Add the new drill
        PracticePlanDrillEntity practicePlanDrill = new PracticePlanDrillEntity();
        practicePlanDrill.setPracticePlan(practicePlan);
        practicePlanDrill.setDrill(drill);
        practicePlanDrill.setSequenceOrder(domain.getSequenceOrder());
        practicePlanDrill.setDurationMinutes(domain.getDurationMinutes());
        practicePlanDrill.setNotes(domain.getNotes());

        practicePlanDrillRepository.save(practicePlanDrill);

        // Recalculate total duration
        updateTotalDuration(practicePlanId);
    }

    @Override
    @Transactional
    public void removeDrillFromPracticePlan(Long practicePlanId, Long practicePlanDrillId) {
        PracticePlanDrillEntity practicePlanDrill = practicePlanDrillRepository.findById(practicePlanDrillId)
                .orElseThrow(() -> new ResourceNotFoundException("Practice Plan Drill not found with id: " + practicePlanDrillId));

        if (!practicePlanDrill.getPracticePlan().getId().equals(practicePlanId)) {
            throw new ResourceNotFoundException("Practice Plan Drill with id " + practicePlanDrillId +
                    " does not belong to Practice Plan with id " + practicePlanId);
        }

        int removedSequence = practicePlanDrill.getSequenceOrder();

        // Delete the drill
        practicePlanDrillRepository.delete(practicePlanDrill);

        // Shift all drills after the removed one
        List<PracticePlanDrillEntity> drillsToShift = practicePlanDrillRepository.findByPracticePlanId(practicePlanId).stream()
                .filter(ppd -> ppd.getSequenceOrder() > removedSequence)
                .sorted(Comparator.comparing(PracticePlanDrillEntity::getSequenceOrder))
                .collect(Collectors.toList());

        for (PracticePlanDrillEntity drillToShift : drillsToShift) {
            drillToShift.setSequenceOrder(drillToShift.getSequenceOrder() - 1);
            practicePlanDrillRepository.save(drillToShift);
        }

        // Recalculate total duration
        updateTotalDuration(practicePlanId);
    }

    @Override
    @Transactional
    public void reorderDrillsInPracticePlan(Long practicePlanId, List<PracticePlanDrill> orderedDrills) {
        // Verify all drills belong to this practice plan
        List<PracticePlanDrillEntity> currentDrills = practicePlanDrillRepository.findByPracticePlanId(practicePlanId);

        Set<Long> currentDrillIds = currentDrills.stream()
                .map(PracticePlanDrillEntity::getId)
                .collect(Collectors.toSet());

        for (PracticePlanDrill drill : orderedDrills) {
            if (!currentDrillIds.contains(drill.getId())) {
                throw new ResourceNotFoundException("Practice Plan Drill with id " + drill.getId() +
                        " does not belong to Practice Plan with id " + practicePlanId);
            }
        }

        // Update sequence orders
        for (int i = 0; i < orderedDrills.size(); i++) {
            PracticePlanDrill drill = orderedDrills.get(i);
            PracticePlanDrillEntity currentDrill = currentDrills.stream()
                    .filter(d -> d.getId().equals(drill.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Practice Plan Drill not found with id: " + drill.getId()));

            currentDrill.setSequenceOrder(i + 1);
            practicePlanDrillRepository.save(currentDrill);
        }
    }

    @Override
    public List<PracticePlanEquipmentSummary> getEquipmentSummaryForPracticePlan(Long practicePlanId) {
        if (!practicePlanRepository.existsById(practicePlanId)) {
            throw new ResourceNotFoundException("Practice Plan not found with id: " + practicePlanId);
        }

        // Get all drills in this practice plan
        List<PracticePlanDrillEntity> practicePlanDrills = practicePlanDrillRepository.findByPracticePlanId(practicePlanId);

        // Get all equipment for these drills
        Map<Long, PracticePlanEquipmentSummary> equipmentSummary = new HashMap<>();

        for (PracticePlanDrillEntity practicePlanDrill : practicePlanDrills) {
            DrillEntity drill = practicePlanDrill.getDrill();
            List<DrillEquipmentEntity> drillEquipments = drillEquipmentRepository.findByDrillId(drill.getId());

            for (DrillEquipmentEntity drillEquipment : drillEquipments) {
                EquipmentEntity equipment = drillEquipment.getEquipment();
                Long equipmentId = equipment.getId();

                if (equipmentSummary.containsKey(equipmentId)) {
                    // Update quantity
                    PracticePlanEquipmentSummary summary = equipmentSummary.get(equipmentId);
                    summary.setTotalQuantity(summary.getTotalQuantity() + drillEquipment.getQuantity());
                } else {
                    // Add new entry
                    PracticePlanEquipmentSummary summary = new PracticePlanEquipmentSummary();
                    summary.setEquipmentId(equipmentId);
                    summary.setEquipmentName(equipment.getName());
                    summary.setTotalQuantity(drillEquipment.getQuantity());
                    equipmentSummary.put(equipmentId, summary);
                }
            }
        }

        return new ArrayList<>(equipmentSummary.values());
    }

    private void updateTotalDuration(Long practicePlanId) {
        PracticePlanEntity practicePlan = practicePlanRepository.findById(practicePlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Practice Plan not found with id: " + practicePlanId));

        List<PracticePlanDrillEntity> practicePlanDrills = practicePlanDrillRepository.findByPracticePlanId(practicePlanId);

        int totalDuration = practicePlanDrills.stream()
                .filter(ppd -> ppd.getDurationMinutes() != null)
                .mapToInt(PracticePlanDrillEntity::getDurationMinutes)
                .sum();

        practicePlan.setTotalDurationMinutes(totalDuration);
        practicePlanRepository.save(practicePlan);
    }

    private PracticePlan mapToDto(PracticePlanEntity practicePlan) {
        PracticePlan dto = new PracticePlan();
        dto.setId(practicePlan.getId());
        dto.setName(practicePlan.getName());
        dto.setDescription(practicePlan.getDescription());
        dto.setSportId(practicePlan.getSport().getId());
        dto.setSportName(practicePlan.getSport().getName());

        if (practicePlan.getFocusArea() != null) {
            dto.setFocusAreaId(practicePlan.getFocusArea().getId());
            dto.setFocusAreaName(practicePlan.getFocusArea().getName());
        }

        dto.setTotalDurationMinutes(practicePlan.getTotalDurationMinutes());

        // Get drills
        List<PracticePlanDrillEntity> practicePlanDrills = practicePlanDrillRepository.findByPracticePlanIdOrderBySequenceOrderAsc(practicePlan.getId());
        Set<PracticePlanDrill> drillDtos = practicePlanDrills.stream()
                .map(ppd -> {
                    PracticePlanDrill ppDto = new PracticePlanDrill();
                    ppDto.setId(ppd.getId());
                    ppDto.setPracticePlanId(ppd.getPracticePlan().getId());
                    ppDto.setDrillId(ppd.getDrill().getId());
                    ppDto.setSequenceOrder(ppd.getSequenceOrder());
                    ppDto.setDurationMinutes(ppd.getDurationMinutes());
                    ppDto.setNotes(ppd.getNotes());

                    // Include drill details
                    Drill drillDto = new Drill();
                    drillDto.setId(ppd.getDrill().getId());
                    drillDto.setName(ppd.getDrill().getName());
                    drillDto.setDescription(ppd.getDrill().getDescription());
                    drillDto.setInstructions(ppd.getDrill().getInstructions());
                    drillDto.setDurationMinutes(ppd.getDrill().getDurationMinutes());
                    drillDto.setDifficultyLevel(ppd.getDrill().getDifficultyLevel());
                    drillDto.setFocusAreaId(ppd.getDrill().getFocusArea().getId());
                    drillDto.setFocusAreaName(ppd.getDrill().getFocusArea().getName());

                    ppDto.setDrill(drillDto);

                    return ppDto;
                })
                .collect(Collectors.toSet());

        dto.setDrills(drillDtos);
        dto.setCreatedAt(practicePlan.getCreatedAt());
        dto.setUpdatedAt(practicePlan.getUpdatedAt());

        return dto;
    }
}
