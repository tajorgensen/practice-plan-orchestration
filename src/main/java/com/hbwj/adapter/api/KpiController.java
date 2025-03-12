package com.hbwj.adapter.api;

import com.hbwj.domain.model.Kpi;
import com.hbwj.domain.port.KpiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/kpis")
@Tag(name = "KPIs", description = "Key Performance Indicators Management API")
public class KpiController {

    private final KpiService kpiService;

    @Autowired
    public KpiController(KpiService kpiService) {
        this.kpiService = kpiService;
    }

    @GetMapping
    @Operation(summary = "Get all KPIs")
    public ResponseEntity<List<Kpi>> getAllKpis() {
        return ResponseEntity.ok(kpiService.getAllKpis());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a KPI by ID")
    public ResponseEntity<Kpi> getKpiById(@PathVariable Long id) {
        return ResponseEntity.ok(kpiService.getKpiById(id));
    }

    @GetMapping("/subcategory/{subcategoryId}")
    @Operation(summary = "Get KPIs by subcategory ID")
    public ResponseEntity<List<Kpi>> getKpisBySubCategoryId(@PathVariable Long subcategoryId) {
        return ResponseEntity.ok(kpiService.getKpisBySubCategoryId(subcategoryId));
    }

    @GetMapping("/sport/{sportId}")
    @Operation(summary = "Get KPIs by sport ID")
    public ResponseEntity<List<Kpi>> getKpisBySportId(@PathVariable Long sportId) {
        return ResponseEntity.ok(kpiService.getKpisBySportId(sportId));
    }

    @GetMapping("/focus-area/{focusAreaId}")
    @Operation(summary = "Get KPIs by focus area ID")
    public ResponseEntity<List<Kpi>> getKpisByFocusAreaId(@PathVariable Long focusAreaId) {
        return ResponseEntity.ok(kpiService.getKpisByFocusAreaId(focusAreaId));
    }

    @PostMapping
    @Operation(summary = "Create a new KPI")
    public ResponseEntity<Kpi> createKpi(@Valid @RequestBody Kpi kpiDto) {
        return new ResponseEntity<>(kpiService.createKpi(kpiDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a KPI")
    public ResponseEntity<Kpi> updateKpi(@PathVariable Long id, @Valid @RequestBody Kpi kpiDto) {
        return ResponseEntity.ok(kpiService.updateKpi(id, kpiDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a KPI")
    public ResponseEntity<Void> deleteKpi(@PathVariable Long id) {
        kpiService.deleteKpi(id);
        return ResponseEntity.noContent().build();
    }
}
