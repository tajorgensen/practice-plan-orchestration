package com.hbwj.adapter.api;

import com.hbwj.domain.model.Sport;
import com.hbwj.domain.port.SportService;
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
@RequestMapping("/api/sports")
@Tag(name = "Sports", description = "Sports Management API")
public class SportController {

    private final SportService sportService;

    @Autowired
    public SportController(SportService sportService) {
        this.sportService = sportService;
    }

    @GetMapping
    @Operation(summary = "Get all sports")
    public ResponseEntity<List<Sport >> getAllSports() {
        return ResponseEntity.ok(sportService.getAllSports());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a sport by ID")
    public ResponseEntity<Sport > getSportById(@PathVariable Long id) {
        return ResponseEntity.ok(sportService.getSportById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new sport")
    public ResponseEntity<Sport > createSport(@Valid @RequestBody Sport sportDto) {
        return new ResponseEntity<>(sportService.createSport(sportDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a sport")
    public ResponseEntity<Sport > updateSport(@PathVariable Long id, @Valid @RequestBody Sport sportDto) {
        return ResponseEntity.ok(sportService.updateSport(id, sportDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a sport")
    public ResponseEntity<Void> deleteSport(@PathVariable Long id) {
        sportService.deleteSport(id);
        return ResponseEntity.noContent().build();
    }
}

