package com.hbwj.adapter.api;

import com.hbwj.domain.model.Drill;
import com.hbwj.domain.model.DrillDetail;
import com.hbwj.domain.model.DrillEquipment;
import com.hbwj.domain.model.Position;
import com.hbwj.domain.port.DrillEquipmentService;
import com.hbwj.domain.port.DrillService;
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
@RequestMapping("/api/drills")
@Tag(name = "Drills", description = "Drills Management API")
public class DrillController {

    private final DrillService drillService;
    private final DrillEquipmentService drillEquipmentService;

    @Autowired
    public DrillController(DrillService drillService, DrillEquipmentService drillEquipmentService) {
        this.drillService = drillService;
        this.drillEquipmentService = drillEquipmentService;
    }

    @GetMapping
    @Operation(summary = "Get all drills")
    public ResponseEntity<List<Drill>> getAllDrills() {
        return ResponseEntity.ok(drillService.getAllDrills());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a drill by ID")
    public ResponseEntity<Drill> getDrillById(@PathVariable Long id) {
        return ResponseEntity.ok(drillService.getDrillById(id));
    }

    @GetMapping("/{id}/details")
    @Operation(summary = "Get drill details by ID")
    public ResponseEntity<DrillDetail> getDrillDetailsById(@PathVariable Long id) {
        return ResponseEntity.ok(drillService.getDrillDetailsById(id));
    }

    @GetMapping("/focus-area/{focusAreaId}")
    @Operation(summary = "Get drills by focus area ID")
    public ResponseEntity<List<Drill>> getDrillsByFocusAreaId(@PathVariable Long focusAreaId) {
        return ResponseEntity.ok(drillService.getDrillsByFocusAreaId(focusAreaId));
    }

    @GetMapping("/sport/{sportId}")
    @Operation(summary = "Get drills by sport ID")
    public ResponseEntity<List<Drill>> getDrillsBySportId(@PathVariable Long sportId) {
        return ResponseEntity.ok(drillService.getDrillsBySportId(sportId));
    }

    @GetMapping("/position/{positionId}")
    @Operation(summary = "Get drills by position ID")
    public ResponseEntity<List<Drill>> getDrillsByPositionId(@PathVariable Long positionId) {
        return ResponseEntity.ok(drillService.getDrillsByPositionId(positionId));
    }

    @GetMapping("/kpi/{kpiId}")
    @Operation(summary = "Get drills by KPI ID")
    public ResponseEntity<List<Drill>> getDrillsByKpiId(@PathVariable Long kpiId) {
        return ResponseEntity.ok(drillService.getDrillsByKpiId(kpiId));
    }

    @GetMapping("/sport/{sportId}/focus-area/{focusAreaId}")
    @Operation(summary = "Get drills by sport ID and focus area ID")
    public ResponseEntity<List<Drill>> getDrillsBySportIdAndFocusAreaId(
            @PathVariable Long sportId, @PathVariable Long focusAreaId) {
        return ResponseEntity.ok(drillService.getDrillsBySportIdAndFocusAreaId(sportId, focusAreaId));
    }

    @PostMapping
    @Operation(summary = "Create a new drill")
    public ResponseEntity<Drill> createDrill(@Valid @RequestBody Drill drillDto) {
        return new ResponseEntity<>(drillService.createDrill(drillDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a drill")
    public ResponseEntity<Drill> updateDrill(@PathVariable Long id, @Valid @RequestBody Drill drillDto) {
        return ResponseEntity.ok(drillService.updateDrill(id, drillDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a drill")
    public ResponseEntity<Void> deleteDrill(@PathVariable Long id) {
        drillService.deleteDrill(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/equipment")
    @Operation(summary = "Add equipment to a drill")
    public ResponseEntity<Void> addEquipmentToDrill(
            @PathVariable Long id, @Valid @RequestBody DrillEquipment drillEquipmentDto) {
        drillService.addEquipmentToDrill(id, drillEquipmentDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{drillId}/equipment/{equipmentId}")
    @Operation(summary = "Remove equipment from a drill")
    public ResponseEntity<Void> removeEquipmentFromDrill(
            @PathVariable Long drillId, @PathVariable Long equipmentId) {
        drillService.removeEquipmentFromDrill(drillId, equipmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/equipment")
    @Operation(summary = "Get equipment for a drill")
    public ResponseEntity<List<DrillEquipment>> getDrillEquipmentByDrillId(@PathVariable Long id) {
        return ResponseEntity.ok(drillEquipmentService.getDrillEquipmentByDrillId(id));
    }

    @PostMapping("/{id}/positions/{positionId}")
    @Operation(summary = "Add a position to a drill")
    public ResponseEntity<Void> addPositionToDrill(
            @PathVariable Long id, @PathVariable Long positionId) {
        drillService.addPositionToDrill(id, positionId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{drillId}/positions/{positionId}")
    @Operation(summary = "Remove a position from a drill")
    public ResponseEntity<Void> removePositionFromDrill(
            @PathVariable Long drillId, @PathVariable Long positionId) {
        drillService.removePositionFromDrill(drillId, positionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/positions")
    @Operation(summary = "Get positions for a drill")
    public ResponseEntity<List<Position>> getDrillPositions(@PathVariable Long id) {
        return ResponseEntity.ok(drillService.getDrillPositions(id));
    }
}
