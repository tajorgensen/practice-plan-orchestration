package com.hbwj.adapter.api;

import com.hbwj.domain.model.Position;
import com.hbwj.domain.port.PositionService;
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
@RequestMapping("/api/positions")
@Tag(name = "Positions", description = "Positions Management API")
public class PositionController {

    private final PositionService positionService;

    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    @Operation(summary = "Get all positions")
    public ResponseEntity<List<Position>> getAllPositions() {
        return ResponseEntity.ok(positionService.getAllPositions());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a position by ID")
    public ResponseEntity<Position> getPositionById(@PathVariable Long id) {
        return ResponseEntity.ok(positionService.getPositionById(id));
    }

    @GetMapping("/sport/{sportId}")
    @Operation(summary = "Get positions by sport ID")
    public ResponseEntity<List<Position>> getPositionsBySportId(@PathVariable Long sportId) {
        return ResponseEntity.ok(positionService.getPositionsBySportId(sportId));
    }

    @PostMapping
    @Operation(summary = "Create a new position")
    public ResponseEntity<Position> createPosition(@Valid @RequestBody Position positionDto) {
        return new ResponseEntity<>(positionService.createPosition(positionDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a position")
    public ResponseEntity<Position> updatePosition(@PathVariable Long id, @Valid @RequestBody Position positionDto) {
        return ResponseEntity.ok(positionService.updatePosition(id, positionDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a position")
    public ResponseEntity<Void> deletePosition(@PathVariable Long id) {
        positionService.deletePosition(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sport/{sportId}/type/{positionType}")
    @Operation(summary = "Get positions by sport ID and position type")
    public ResponseEntity<List<Position>> getPositionsBySportIdAndPositionType(
            @PathVariable Long sportId, @PathVariable String positionType) {
        return ResponseEntity.ok(positionService.getPositionsBySportIdAndPositionType(sportId, positionType));
    }

    @GetMapping("/sport/{sportId}/type/{positionType}/including-both")
    @Operation(summary = "Get positions by sport ID and position type, including 'BOTH' positions")
    public ResponseEntity<List<Position>> getPositionsBySportIdAndPositionTypeIncludingBoth(
            @PathVariable Long sportId, @PathVariable String positionType) {
        return ResponseEntity.ok(positionService.getPositionsBySportIdAndPositionTypeIncludingBoth(sportId, positionType));
    }
}
