package com.hbwj.adapter.gateway.service;

import com.hbwj.adapter.gateway.repository.DrillEquipmentRepository;
import com.hbwj.adapter.gateway.repository.DrillKpiRepository;
import com.hbwj.adapter.gateway.repository.DrillRepository;
import com.hbwj.adapter.gateway.repository.EquipmentRepository;
import com.hbwj.adapter.gateway.repository.FocusAreaRepository;
import com.hbwj.adapter.gateway.repository.KpiRepository;
import com.hbwj.adapter.gateway.repository.PositionRepository;
import com.hbwj.adapter.gateway.repository.SportRepository;
import com.hbwj.adapter.gateway.repository.entity.DrillEntity;
import com.hbwj.adapter.gateway.repository.entity.DrillEquipmentEntity;
import com.hbwj.adapter.gateway.repository.entity.DrillKpiEntity;
import com.hbwj.adapter.gateway.repository.entity.EquipmentEntity;
import com.hbwj.adapter.gateway.repository.entity.FocusAreaEntity;
import com.hbwj.adapter.gateway.repository.entity.KpiEntity;
import com.hbwj.adapter.gateway.repository.entity.PositionEntity;
import com.hbwj.adapter.gateway.repository.entity.SportEntity;
import com.hbwj.domain.model.Drill;
import com.hbwj.domain.model.DrillDetail;
import com.hbwj.domain.model.DrillEquipment;
import com.hbwj.domain.model.Kpi;
import com.hbwj.domain.model.Position;
import com.hbwj.domain.model.Sport;
import com.hbwj.domain.port.DrillService;
import com.hbwj.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DrillServiceImpl implements DrillService {

    private final DrillRepository drillRepository;
    private final FocusAreaRepository focusAreaRepository;
    private final SportRepository sportRepository;
    private final PositionRepository positionRepository;
    private final KpiRepository kpiRepository;
    private final DrillEquipmentRepository drillEquipmentRepository;
    private final DrillKpiRepository drillKpiRepository;
    private final EquipmentRepository equipmentRepository;

    @Autowired
    public DrillServiceImpl(
            DrillRepository drillRepository,
            FocusAreaRepository focusAreaRepository,
            SportRepository sportRepository,
            PositionRepository positionRepository,
            KpiRepository kpiRepository,
            DrillEquipmentRepository drillEquipmentRepository,
            DrillKpiRepository drillKpiRepository,
            EquipmentRepository equipmentRepository) {
        this.drillRepository = drillRepository;
        this.focusAreaRepository = focusAreaRepository;
        this.sportRepository = sportRepository;
        this.positionRepository = positionRepository;
        this.kpiRepository = kpiRepository;
        this.drillEquipmentRepository = drillEquipmentRepository;
        this.drillKpiRepository = drillKpiRepository;
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public List<Drill> getAllDrills() {
        return drillRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Drill> getDrillsByFocusAreaId(Long focusAreaId) {
        return drillRepository.findByFocusAreaId(focusAreaId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Drill> getDrillsBySportId(Long sportId) {
        return drillRepository.findBySportId(sportId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Drill> getDrillsByPositionId(Long positionId) {
        return drillRepository.findByPositionId(positionId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Drill> getDrillsByKpiId(Long kpiId) {
        return drillRepository.findByKpiId(kpiId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Drill> getDrillsBySportIdAndFocusAreaId(Long sportId, Long focusAreaId) {
        return drillRepository.findBySportIdAndFocusAreaId(sportId, focusAreaId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Drill getDrillById(Long id) {
        DrillEntity drill = drillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drill not found with id: " + id));
        return mapToDto(drill);
    }

    @Override
    public DrillDetail getDrillDetailsById(Long id) {
        DrillEntity drill = drillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drill not found with id: " + id));
        return mapToDetailDto(drill);
    }

    @Override
    @Transactional
    public Drill createDrill(Drill domain) {
        FocusAreaEntity focusArea = focusAreaRepository.findById(domain.getFocusAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Focus Area not found with id: " + domain.getFocusAreaId()));

        DrillEntity drill = new DrillEntity();
        drill.setName(domain.getName());
        drill.setDescription(domain.getDescription());
        drill.setInstructions(domain.getInstructions());
        drill.setDurationMinutes(domain.getDurationMinutes());
        drill.setDifficultyLevel(domain.getDifficultyLevel());
        drill.setFocusArea(focusArea);

        // Save the drill first to get an ID
        DrillEntity savedDrill = drillRepository.save(drill);

        // Add sports
        if (domain.getSportIds() != null && !domain.getSportIds().isEmpty()) {
            Set<SportEntity> sports = domain.getSportIds().stream()
                    .map(sportId -> sportRepository.findById(sportId)
                            .orElseThrow(() -> new ResourceNotFoundException("Sport not found with id: " + sportId)))
                    .collect(Collectors.toSet());
            savedDrill.setSports(sports);
        }

        // Add positions
        if (domain.getPositionIds() != null && !domain.getPositionIds().isEmpty()) {
            Set<PositionEntity> positions = domain.getPositionIds().stream()
                    .map(positionId -> positionRepository.findById(positionId)
                            .orElseThrow(() -> new ResourceNotFoundException("Position not found with id: " + positionId)))
                    .collect(Collectors.toSet());
            savedDrill.setPositions(positions);
        }

        // Add KPIs
        if (domain.getKpiIds() != null && !domain.getKpiIds().isEmpty()) {
            Set<KpiEntity> kpis = domain.getKpiIds().stream()
                    .map(kpiId -> kpiRepository.findById(kpiId)
                            .orElseThrow(() -> new ResourceNotFoundException("KPI not found with id: " + kpiId)))
                    .collect(Collectors.toSet());
            savedDrill.setKpis(kpis);
        }

        // Update the drill with the relationships
        savedDrill = drillRepository.save(savedDrill);

        // Handle equipment if provided
        if (domain.getEquipment() != null && !domain.getEquipment().isEmpty()) {
            for (DrillEquipment equipmentDto : domain.getEquipment()) {
                EquipmentEntity equipment = equipmentRepository.findById(equipmentDto.getEquipmentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + equipmentDto.getEquipmentId()));

                DrillEquipmentEntity drillEquipment = new DrillEquipmentEntity();
                drillEquipment.setDrill(savedDrill);
                drillEquipment.setEquipment(equipment);
                drillEquipment.setQuantity(equipmentDto.getQuantity());

                drillEquipmentRepository.save(drillEquipment);
            }
        }

        return mapToDto(savedDrill);
    }

    @Override
    @Transactional
    public Drill updateDrill(Long id, Drill domain) {
        DrillEntity drill = drillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drill not found with id: " + id));

        FocusAreaEntity focusArea = focusAreaRepository.findById(domain.getFocusAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Focus Area not found with id: " + domain.getFocusAreaId()));

        drill.setName(domain.getName());
        drill.setDescription(domain.getDescription());
        drill.setInstructions(domain.getInstructions());
        drill.setDurationMinutes(domain.getDurationMinutes());
        drill.setDifficultyLevel(domain.getDifficultyLevel());
        drill.setFocusArea(focusArea);

        // Update sports
        if (domain.getSportIds() != null) {
            Set<SportEntity> sports = domain.getSportIds().stream()
                    .map(sportId -> sportRepository.findById(sportId)
                            .orElseThrow(() -> new ResourceNotFoundException("Sport not found with id: " + sportId)))
                    .collect(Collectors.toSet());
            drill.setSports(sports);
        }

        // Update positions
        if (domain.getPositionIds() != null) {
            Set<PositionEntity> positions = domain.getPositionIds().stream()
                    .map(positionId -> positionRepository.findById(positionId)
                            .orElseThrow(() -> new ResourceNotFoundException("Position not found with id: " + positionId)))
                    .collect(Collectors.toSet());
            drill.setPositions(positions);
        }

        // Update KPIs
        if (domain.getKpiIds() != null) {
            Set<KpiEntity> kpis = domain.getKpiIds().stream()
                    .map(kpiId -> kpiRepository.findById(kpiId)
                            .orElseThrow(() -> new ResourceNotFoundException("KPI not found with id: " + kpiId)))
                    .collect(Collectors.toSet());
            drill.setKpis(kpis);
        }

        DrillEntity updatedDrill = drillRepository.save(drill);

        // Handle equipment updates if provided
        if (domain.getEquipment() != null) {
            // Get current equipment associations
            List<DrillEquipmentEntity> currentEquipment = drillEquipmentRepository.findByDrillId(id);

            // Create a map of equipment ID to DrillEquipment for easy lookup
            Map<Long, DrillEquipmentEntity> currentEquipmentMap = currentEquipment.stream()
                    .collect(Collectors.toMap(
                            de -> de.getEquipment().getId(),
                            de -> de
                    ));

            // Process each equipment in the DTO
            for (DrillEquipment equipmentDto : domain.getEquipment()) {
                if (currentEquipmentMap.containsKey(equipmentDto.getEquipmentId())) {
                    // Update existing equipment association
                    DrillEquipmentEntity drillEquipment = currentEquipmentMap.get(equipmentDto.getEquipmentId());
                    drillEquipment.setQuantity(equipmentDto.getQuantity());
                    drillEquipmentRepository.save(drillEquipment);

                    // Remove from the map to track what's been processed
                    currentEquipmentMap.remove(equipmentDto.getEquipmentId());
                } else {
                    // Add new equipment association
                    EquipmentEntity equipment = equipmentRepository.findById(equipmentDto.getEquipmentId())
                            .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + equipmentDto.getEquipmentId()));

                    DrillEquipmentEntity drillEquipment = new DrillEquipmentEntity();
                    drillEquipment.setDrill(updatedDrill);
                    drillEquipment.setEquipment(equipment);
                    drillEquipment.setQuantity(equipmentDto.getQuantity());

                    drillEquipmentRepository.save(drillEquipment);
                }
            }

            // Remove any equipment associations that were not in the update
            for (DrillEquipmentEntity drillEquipment : currentEquipmentMap.values()) {
                drillEquipmentRepository.delete(drillEquipment);
            }
        }

        return mapToDto(updatedDrill);
    }

    @Override
    @Transactional
    public void deleteDrill(Long id) {
        if (!drillRepository.existsById(id)) {
            throw new ResourceNotFoundException("Drill not found with id: " + id);
        }

        // Delete related drill equipment records first
        List<DrillEquipmentEntity> drillEquipment = drillEquipmentRepository.findByDrillId(id);
        drillEquipmentRepository.deleteAll(drillEquipment);

        // Delete related drill KPI records
        List<DrillKpiEntity> drillKpis = drillKpiRepository.findByDrillId(id);
        drillKpiRepository.deleteAll(drillKpis);

        // Now delete the drill
        drillRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addEquipmentToDrill(Long drillId, DrillEquipment domain) {
        DrillEntity drill = drillRepository.findById(drillId)
                .orElseThrow(() -> new ResourceNotFoundException("Drill not found with id: " + drillId));

        EquipmentEntity equipment = equipmentRepository.findById(domain.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + domain.getEquipmentId()));

        // Check if this equipment is already associated with the drill
        Optional<DrillEquipmentEntity> existingDrillEquipment = drillEquipmentRepository.findByDrillId(drillId).stream()
                .filter(de -> de.getEquipment().getId().equals(equipment.getId()))
                .findFirst();

        if (existingDrillEquipment.isPresent()) {
            // Update quantity
            DrillEquipmentEntity drillEquipment = existingDrillEquipment.get();
            drillEquipment.setQuantity(domain.getQuantity());
            drillEquipmentRepository.save(drillEquipment);
        } else {
            // Create new association
            DrillEquipmentEntity drillEquipment = new DrillEquipmentEntity();
            drillEquipment.setDrill(drill);
            drillEquipment.setEquipment(equipment);
            drillEquipment.setQuantity(domain.getQuantity());
            drillEquipmentRepository.save(drillEquipment);
        }
    }

    @Override
    @Transactional
    public void removeEquipmentFromDrill(Long drillId, Long equipmentId) {
        List<DrillEquipmentEntity> drillEquipments = drillEquipmentRepository.findByDrillId(drillId).stream()
                .filter(de -> de.getEquipment().getId().equals(equipmentId))
                .collect(Collectors.toList());

        if (drillEquipments.isEmpty()) {
            throw new ResourceNotFoundException("Equipment with id " + equipmentId + " not found for drill with id " + drillId);
        }

        drillEquipmentRepository.deleteAll(drillEquipments);
    }

    private Drill mapToDto(DrillEntity drill) {
        Drill domain = new Drill();
        domain.setId(drill.getId());
        domain.setName(drill.getName());
        domain.setDescription(drill.getDescription());
        domain.setInstructions(drill.getInstructions());
        domain.setDurationMinutes(drill.getDurationMinutes());
        domain.setDifficultyLevel(drill.getDifficultyLevel());
        domain.setFocusAreaId(drill.getFocusArea().getId());
        domain.setFocusAreaName(drill.getFocusArea().getName());

        // Set related IDs
        domain.setSportIds(drill.getSports().stream()
                .map(SportEntity::getId)
                .collect(Collectors.toSet()));

        domain.setPositionIds(drill.getPositions().stream()
                .map(PositionEntity::getId)
                .collect(Collectors.toSet()));

        domain.setKpiIds(drill.getKpis().stream()
                .map(KpiEntity::getId)
                .collect(Collectors.toSet()));

        // Get equipment
        List<DrillEquipmentEntity> drillEquipments = drillEquipmentRepository.findByDrillId(drill.getId());
        Set<DrillEquipment> equipmentDtos = drillEquipments.stream()
                .map(de -> {
                    DrillEquipment deDto = new DrillEquipment();
                    deDto.setId(de.getId());
                    deDto.setDrillId(de.getDrill().getId());
                    deDto.setEquipmentId(de.getEquipment().getId());
                    deDto.setQuantity(de.getQuantity());
                    deDto.setDrillName(de.getDrill().getName());
                    deDto.setEquipmentName(de.getEquipment().getName());
                    deDto.setCreatedAt(de.getCreatedAt());
                    deDto.setUpdatedAt(de.getUpdatedAt());
                    return deDto;
                })
                .collect(Collectors.toSet());

        domain.setEquipment(equipmentDtos);
        domain.setCreatedAt(drill.getCreatedAt());
        domain.setUpdatedAt(drill.getUpdatedAt());

        return domain;
    }

    private DrillDetail mapToDetailDto(DrillEntity drill) {
        DrillDetail dto = new DrillDetail();
        dto.setId(drill.getId());
        dto.setName(drill.getName());
        dto.setDescription(drill.getDescription());
        dto.setInstructions(drill.getInstructions());
        dto.setDurationMinutes(drill.getDurationMinutes());
        dto.setDifficultyLevel(drill.getDifficultyLevel());
        dto.setFocusAreaId(drill.getFocusArea().getId());
        dto.setFocusAreaName(drill.getFocusArea().getName());

        // Set related IDs
        dto.setSportIds(drill.getSports().stream()
                .map(SportEntity::getId)
                .collect(Collectors.toSet()));

        dto.setPositionIds(drill.getPositions().stream()
                .map(PositionEntity::getId)
                .collect(Collectors.toSet()));

        dto.setKpiIds(drill.getKpis().stream()
                .map(KpiEntity::getId)
                .collect(Collectors.toSet()));

        // Get equipment
        List<DrillEquipmentEntity> drillEquipments = drillEquipmentRepository.findByDrillId(drill.getId());
        Set<DrillEquipment> equipmentDtos = drillEquipments.stream()
                .map(de -> {
                    DrillEquipment deDto = new DrillEquipment();
                    deDto.setId(de.getId());
                    deDto.setDrillId(de.getDrill().getId());
                    deDto.setEquipmentId(de.getEquipment().getId());
                    deDto.setQuantity(de.getQuantity());
                    deDto.setDrillName(de.getDrill().getName());
                    deDto.setEquipmentName(de.getEquipment().getName());
                    deDto.setCreatedAt(de.getCreatedAt());
                    deDto.setUpdatedAt(de.getUpdatedAt());
                    return deDto;
                })
                .collect(Collectors.toSet());

        dto.setEquipment(equipmentDtos);

        // Add full objects for detail view
        dto.setSports(drill.getSports().stream()
                .map(sport -> {
                    Sport sportDto = new Sport();
                    sportDto.setId(sport.getId());
                    sportDto.setName(sport.getName());
                    sportDto.setDescription(sport.getDescription());
                    return sportDto;
                })
                .collect(Collectors.toSet()));

        dto.setPositions(drill.getPositions().stream()
                .map(position -> {
                    Position positionDto = new Position();
                    positionDto.setId(position.getId());
                    positionDto.setName(position.getName());
                    positionDto.setDescription(position.getDescription());
                    positionDto.setSportId(position.getSport().getId());
                    positionDto.setSportName(position.getSport().getName());
                    return positionDto;
                })
                .collect(Collectors.toSet()));

        dto.setKpis(drill.getKpis().stream()
                .map(kpi -> {
                    Kpi kpiDto = new Kpi();
                    kpiDto.setId(kpi.getId());
                    kpiDto.setName(kpi.getName());
                    kpiDto.setDescription(kpi.getDescription());
                    kpiDto.setMeasurementUnit(kpi.getMeasurementUnit());
                    kpiDto.setTargetValue(kpi.getTargetValue());
                    kpiDto.setSubCategoryId(kpi.getSubCategory().getId());
                    kpiDto.setSubCategoryName(kpi.getSubCategory().getName());

                    // Try to get impact level from DrillKpi if it exists
                    Optional<DrillKpiEntity> drillKpi = drillKpiRepository.findByDrillIdAndKpiId(drill.getId(), kpi.getId());
                    drillKpi.ifPresent(dk -> dto.setImpactLevel(dk.getImpactLevel()));

                    return kpiDto;
                })
                .collect(Collectors.toSet()));

        dto.setCreatedAt(drill.getCreatedAt());
        dto.setUpdatedAt(drill.getUpdatedAt());

        return dto;
    }
}

