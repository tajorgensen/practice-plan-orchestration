package com.hbwj.adapter.gateway.service;

import com.hbwj.adapter.gateway.repository.KpiRepository;
import com.hbwj.adapter.gateway.repository.SubCategoryRepository;
import com.hbwj.adapter.gateway.repository.entity.KpiEntity;
import com.hbwj.adapter.gateway.repository.entity.SubCategoryEntity;
import com.hbwj.domain.model.Kpi;
import com.hbwj.domain.port.KpiService;
import com.hbwj.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KpiServiceImpl implements KpiService {

    private final KpiRepository kpiRepository;
    private final SubCategoryRepository subcategoryRepository;

    @Autowired
    public KpiServiceImpl(
            KpiRepository kpiRepository,
            SubCategoryRepository subcategoryRepository) {
        this.kpiRepository = kpiRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    @Override
    public List<Kpi> getAllKpis() {
        return kpiRepository.findAll().stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public List<Kpi> getKpisBySubCategoryId(Long subCategoryId) {
        return kpiRepository.findBySubCategoryId(subCategoryId).stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public List<Kpi> getKpisBySportId(Long sportId) {
        return kpiRepository.findBySportId(sportId).stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public List<Kpi> getKpisByFocusAreaId(Long focusAreaId) {
        return kpiRepository.findByFocusAreaId(focusAreaId).stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public Kpi getKpiById(Long id) {
        KpiEntity kpi = kpiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KPI not found with id: " + id));
        return mapTo(kpi);
    }

    @Override
    @Transactional
    public Kpi createKpi(Kpi domain) {
        SubCategoryEntity subcategory = subcategoryRepository.findById(domain.getSubCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found with id: " + domain.getSubCategoryId()));

        KpiEntity kpi = new KpiEntity();
        kpi.setName(domain.getName());
        kpi.setDescription(domain.getDescription());
        kpi.setMeasurementUnit(domain.getMeasurementUnit());
        kpi.setTargetValue(domain.getTargetValue());
        kpi.setSubCategory(subcategory);

        KpiEntity savedKpi = kpiRepository.save(kpi);
        return mapTo(savedKpi);
    }

    @Override
    @Transactional
    public Kpi updateKpi(Long id, Kpi domain) {
        KpiEntity kpi = kpiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KPI not found with id: " + id));

        SubCategoryEntity subcategory = subcategoryRepository.findById(domain.getSubCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found with id: " + domain.getSubCategoryId()));

        kpi.setName(domain.getName());
        kpi.setDescription(domain.getDescription());
        kpi.setMeasurementUnit(domain.getMeasurementUnit());
        kpi.setTargetValue(domain.getTargetValue());
        kpi.setSubCategory(subcategory);

        KpiEntity updatedKpi = kpiRepository.save(kpi);
        return mapTo(updatedKpi);
    }

    @Override
    @Transactional
    public void deleteKpi(Long id) {
        if (!kpiRepository.existsById(id)) {
            throw new ResourceNotFoundException("KPI not found with id: " + id);
        }
        kpiRepository.deleteById(id);
    }

    private Kpi mapTo(KpiEntity kpi) {
        Kpi dto = new Kpi();
        dto.setId(kpi.getId());
        dto.setName(kpi.getName());
        dto.setDescription(kpi.getDescription());
        dto.setMeasurementUnit(kpi.getMeasurementUnit());
        dto.setTargetValue(kpi.getTargetValue());
        dto.setSubCategoryId(kpi.getSubCategory().getId());
        dto.setSubCategoryName(kpi.getSubCategory().getName());
        dto.setCategoryName(kpi.getSubCategory().getCategory().getName());
        dto.setSportName(kpi.getSubCategory().getCategory().getSport().getName());
        dto.setFocusAreaName(kpi.getSubCategory().getCategory().getFocusArea().getName());
        dto.setCreatedAt(kpi.getCreatedAt());
        dto.setUpdatedAt(kpi.getUpdatedAt());
        return dto;
    }
}
