package com.hbwj.adapter.api;

import com.hbwj.domain.model.Equipment;
import com.hbwj.domain.port.EquipmentService;
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
@RequestMapping("/api/equipment")
@Tag(name = "Equipment", description = "Equipment Management API")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    @Operation(summary = "Get all equipment")
    public ResponseEntity<List<Equipment>> getAllEquipment() {
        return ResponseEntity.ok(equipmentService.getAllEquipment());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an equipment by ID")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new equipment")
    public ResponseEntity<Equipment> createEquipment(@Valid @RequestBody Equipment equipmentDto) {
        return new ResponseEntity<>(equipmentService.createEquipment(equipmentDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an equipment")
    public ResponseEntity<Equipment> updateEquipment(@PathVariable Long id, @Valid @RequestBody Equipment equipmentDto) {
        return ResponseEntity.ok(equipmentService.updateEquipment(id, equipmentDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an equipment")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }
}
