package com.hbwj.adapter.api;

import com.hbwj.domain.model.PracticePlan;
import com.hbwj.domain.model.PracticePlanDrill;
import com.hbwj.domain.model.PracticePlanEquipmentSummary;
import com.hbwj.domain.port.PracticePlanService;
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
@RequestMapping("/api/practice-plans")
@Tag(name = "Practice Plans", description = "Practice Plans Management API")
public class PracticePlanController {

    private final PracticePlanService practicePlanService;

    @Autowired
    public PracticePlanController(PracticePlanService practicePlanService) {
        this.practicePlanService = practicePlanService;
    }

    @GetMapping
    @Operation(summary = "Get all practice plans")
    public ResponseEntity<List<PracticePlan>> getAllPracticePlans() {
        return ResponseEntity.ok(practicePlanService.getAllPracticePlans());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a practice plan by ID")
    public ResponseEntity<PracticePlan> getPracticePlanById(@PathVariable Long id) {
        return ResponseEntity.ok(practicePlanService.getPracticePlanById(id));
    }

    @GetMapping("/sport/{sportId}")
    @Operation(summary = "Get practice plans by sport ID")
    public ResponseEntity<List<PracticePlan>> getPracticePlansBySportId(@PathVariable Long sportId) {
        return ResponseEntity.ok(practicePlanService.getPracticePlansBySportId(sportId));
    }

    @GetMapping("/focus-area/{focusAreaId}")
    @Operation(summary = "Get practice plans by focus area ID")
    public ResponseEntity<List<PracticePlan>> getPracticePlansByFocusAreaId(@PathVariable Long focusAreaId) {
        return ResponseEntity.ok(practicePlanService.getPracticePlansByFocusAreaId(focusAreaId));
    }

    @GetMapping("/sport/{sportId}/focus-area/{focusAreaId}")
    @Operation(summary = "Get practice plans by sport ID and focus area ID")
    public ResponseEntity<List<PracticePlan>> getPracticePlansBySportIdAndFocusAreaId(
            @PathVariable Long sportId, @PathVariable Long focusAreaId) {
        return ResponseEntity.ok(practicePlanService.getPracticePlansBySportIdAndFocusAreaId(sportId, focusAreaId));
    }

    @PostMapping
    @Operation(summary = "Create a new practice plan")
    public ResponseEntity<PracticePlan> createPracticePlan(@Valid @RequestBody PracticePlan practicePlanDto) {
        return new ResponseEntity<>(practicePlanService.createPracticePlan(practicePlanDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a practice plan")
    public ResponseEntity<PracticePlan> updatePracticePlan(
            @PathVariable Long id, @Valid @RequestBody PracticePlan practicePlanDto) {
        return ResponseEntity.ok(practicePlanService.updatePracticePlan(id, practicePlanDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a practice plan")
    public ResponseEntity<Void> deletePracticePlan(@PathVariable Long id) {
        practicePlanService.deletePracticePlan(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/drills")
    @Operation(summary = "Add a drill to a practice plan")
    public ResponseEntity<Void> addDrillToPracticePlan(
            @PathVariable Long id, @Valid @RequestBody PracticePlanDrill practicePlanDrillDto) {
        practicePlanService.addDrillToPracticePlan(id, practicePlanDrillDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{practicePlanId}/drills/{practicePlanDrillId}")
    @Operation(summary = "Remove a drill from a practice plan")
    public ResponseEntity<Void> removeDrillFromPracticePlan(
            @PathVariable Long practicePlanId, @PathVariable Long practicePlanDrillId) {
        practicePlanService.removeDrillFromPracticePlan(practicePlanId, practicePlanDrillId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/drills/reorder")
    @Operation(summary = "Reorder drills in a practice plan")
    public ResponseEntity<Void> reorderDrillsInPracticePlan(
            @PathVariable Long id, @Valid @RequestBody List<PracticePlanDrill> orderedDrills) {
        practicePlanService.reorderDrillsInPracticePlan(id, orderedDrills);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/equipment")
    @Operation(summary = "Get equipment summary for a practice plan")
    public ResponseEntity<List<PracticePlanEquipmentSummary>> getEquipmentSummaryForPracticePlan(@PathVariable Long id) {
        return ResponseEntity.ok(practicePlanService.getEquipmentSummaryForPracticePlan(id));
    }
}
