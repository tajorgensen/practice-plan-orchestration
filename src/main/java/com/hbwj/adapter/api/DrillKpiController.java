package com.hbwj.adapter.api;

import com.hbwj.domain.model.DrillKpi;
import com.hbwj.domain.port.DrillKpiService;
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
@RequestMapping("/api/drill-kpis")
@Tag(name = "Drill KPIs", description = "Drill KPIs Management API")
public class DrillKpiController {

    private final DrillKpiService drillKpiService;

    @Autowired
    public DrillKpiController(DrillKpiService drillKpiService) {
        this.drillKpiService = drillKpiService;
    }

    @GetMapping
    @Operation(summary = "Get all drill KPIs")
    public ResponseEntity<List<DrillKpi>> getAllDrillKpis() {
        return ResponseEntity.ok(drillKpiService.getAllDrillKpis());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a drill KPI by ID")
    public ResponseEntity<DrillKpi> getDrillKpiById(@PathVariable Long id) {
        return ResponseEntity.ok(drillKpiService.getDrillKpiById(id));
    }

    @GetMapping("/drill/{drillId}")
    @Operation(summary = "Get drill KPIs by drill ID")
    public ResponseEntity<List<DrillKpi>> getDrillKpisByDrillId(@PathVariable Long drillId) {
        return ResponseEntity.ok(drillKpiService.getDrillKpisByDrillId(drillId));
    }

    @GetMapping("/kpi/{kpiId}")
    @Operation(summary = "Get drill KPIs by KPI ID")
    public ResponseEntity<List<DrillKpi>> getDrillKpisByKpiId(@PathVariable Long kpiId) {
        return ResponseEntity.ok(drillKpiService.getDrillKpisByKpiId(kpiId));
    }

    @PostMapping
    @Operation(summary = "Create a new drill KPI relationship")
    public ResponseEntity<DrillKpi> createDrillKpi(@Valid @RequestBody DrillKpi drillKpiDto) {
        return new ResponseEntity<>(drillKpiService.createDrillKpi(drillKpiDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a drill KPI relationship")
    public ResponseEntity<DrillKpi> updateDrillKpi(@PathVariable Long id, @Valid @RequestBody DrillKpi drillKpiDto) {
        return ResponseEntity.ok(drillKpiService.updateDrillKpi(id, drillKpiDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a drill KPI relationship")
    public ResponseEntity<Void> deleteDrillKpi(@PathVariable Long id) {
        drillKpiService.deleteDrillKpi(id);
        return ResponseEntity.noContent().build();
    }
}
