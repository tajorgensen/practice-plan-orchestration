package com.hbwj.adapter.api;

import com.hbwj.domain.model.FocusArea;
import com.hbwj.domain.port.FocusAreaService;
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
@RequestMapping("/api/focus-areas")
@Tag(name = "Focus Areas", description = "Focus Areas Management API")
public class FocusAreaController {

    private final FocusAreaService focusAreaService;

    @Autowired
    public FocusAreaController(FocusAreaService focusAreaService) {
        this.focusAreaService = focusAreaService;
    }

    @GetMapping
    @Operation(summary = "Get all focus areas")
    public ResponseEntity<List<FocusArea>> getAllFocusAreas() {
        return ResponseEntity.ok(focusAreaService.getAllFocusAreas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a focus area by ID")
    public ResponseEntity<FocusArea> getFocusAreaById(@PathVariable Long id) {
        return ResponseEntity.ok(focusAreaService.getFocusAreaById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new focus area")
    public ResponseEntity<FocusArea> createFocusArea(@Valid @RequestBody FocusArea focusAreaDto) {
        return new ResponseEntity<>(focusAreaService.createFocusArea(focusAreaDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a focus area")
    public ResponseEntity<FocusArea> updateFocusArea(@PathVariable Long id, @Valid @RequestBody FocusArea focusAreaDto) {
        return ResponseEntity.ok(focusAreaService.updateFocusArea(id, focusAreaDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a focus area")
    public ResponseEntity<Void> deleteFocusArea(@PathVariable Long id) {
        focusAreaService.deleteFocusArea(id);
        return ResponseEntity.noContent().build();
    }
}
