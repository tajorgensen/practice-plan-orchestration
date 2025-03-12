package com.hbwj.adapter.gateway.service;

import com.hbwj.adapter.gateway.repository.DrillKpiRepository;
import com.hbwj.adapter.gateway.repository.DrillRepository;
import com.hbwj.adapter.gateway.repository.KpiRepository;
import com.hbwj.adapter.gateway.repository.entity.DrillEntity;
import com.hbwj.adapter.gateway.repository.entity.DrillKpiEntity;
import com.hbwj.adapter.gateway.repository.entity.KpiEntity;
import com.hbwj.domain.model.DrillKpi;
import com.hbwj.domain.port.DrillKpiService;
import com.hbwj.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DrillKpiServiceImpl implements DrillKpiService {

    private final DrillKpiRepository drillKpiRepository;
    private final DrillRepository drillRepository;
    private final KpiRepository kpiRepository;

    @Autowired
    public DrillKpiServiceImpl(
            DrillKpiRepository drillKpiRepository,
            DrillRepository drillRepository,
            KpiRepository kpiRepository) {
        this.drillKpiRepository = drillKpiRepository;
        this.drillRepository = drillRepository;
        this.kpiRepository = kpiRepository;
    }

    @Override
    public List<DrillKpi> getAllDrillKpis() {
        return drillKpiRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DrillKpi> getDrillKpisByDrillId(Long drillId) {
        return drillKpiRepository.findByDrillId(drillId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DrillKpi> getDrillKpisByKpiId(Long kpiId) {
        return drillKpiRepository.findByKpiId(kpiId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DrillKpi getDrillKpiById(Long id) {
        DrillKpiEntity drillKpi = drillKpiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drill KPI not found with id: " + id));
        return mapToDto(drillKpi);
    }

    @Override
    @Transactional
    public DrillKpi createDrillKpi(DrillKpi domain) {
        DrillEntity drill = drillRepository.findById(domain.getDrillId())
                .orElseThrow(() -> new ResourceNotFoundException("Drill not found with id: " + domain.getDrillId()));

        KpiEntity kpi = kpiRepository.findById(domain.getKpiId())
                .orElseThrow(() -> new ResourceNotFoundException("KPI not found with id: " + domain.getKpiId()));

        // Check if there's already a relationship
        Optional<DrillKpiEntity> existingRelation = drillKpiRepository.findByDrillIdAndKpiId(drill.getId(), kpi.getId());

        if (existingRelation.isPresent()) {
            // Update existing relation
            DrillKpiEntity drillKpi = existingRelation.get();
            drillKpi.setImpactLevel(domain.getImpactLevel());
            return mapToDto(drillKpiRepository.save(drillKpi));
        }

        // Create new relation
        DrillKpiEntity drillKpi = new DrillKpiEntity();
        drillKpi.setDrill(drill);
        drillKpi.setKpi(kpi);
        drillKpi.setImpactLevel(domain.getImpactLevel());

        // Also add the KPI to the drill's KPI set (many-to-many)
        drill.getKpis().add(kpi);
        drillRepository.save(drill);

        DrillKpiEntity savedDrillKpi = drillKpiRepository.save(drillKpi);
        return mapToDto(savedDrillKpi);
    }

    @Override
    @Transactional
    public DrillKpi updateDrillKpi(Long id, DrillKpi domain) {
        DrillKpiEntity drillKpi = drillKpiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drill KPI not found with id: " + id));

        DrillEntity drill = drillRepository.findById(domain.getDrillId())
                .orElseThrow(() -> new ResourceNotFoundException("Drill not found with id: " + domain.getDrillId()));

        KpiEntity kpi = kpiRepository.findById(domain.getKpiId())
                .orElseThrow(() -> new ResourceNotFoundException("KPI not found with id: " + domain.getKpiId()));

        drillKpi.setDrill(drill);
        drillKpi.setKpi(kpi);
        drillKpi.setImpactLevel(domain.getImpactLevel());

        DrillKpiEntity updatedDrillKpi = drillKpiRepository.save(drillKpi);
        return mapToDto(updatedDrillKpi);
    }

    @Override
    @Transactional
    public void deleteDrillKpi(Long id) {
        DrillKpiEntity drillKpi = drillKpiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drill KPI not found with id: " + id));

        // Remove the KPI from the drill's KPI set if needed
        DrillEntity drill = drillKpi.getDrill();
        KpiEntity kpi = drillKpi.getKpi();

        drill.getKpis().remove(kpi);
        drillRepository.save(drill);

        drillKpiRepository.deleteById(id);
    }

    private DrillKpi mapToDto(DrillKpiEntity drillKpi) {
        DrillKpi domain = new DrillKpi();
        domain.setId(drillKpi.getId());
        domain.setDrillId(drillKpi.getDrill().getId());
        domain.setKpiId(drillKpi.getKpi().getId());
        domain.setImpactLevel(drillKpi.getImpactLevel());
        domain.setDrillName(drillKpi.getDrill().getName());
        domain.setKpiName(drillKpi.getKpi().getName());
        domain.setCreatedAt(drillKpi.getCreatedAt());
        domain.setUpdatedAt(drillKpi.getUpdatedAt());
        return domain;
    }
}
