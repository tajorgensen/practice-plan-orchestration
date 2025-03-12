package com.hbwj.adapter.api;

import com.hbwj.adapter.model.PracticePlanGenerateRequest;
import com.hbwj.adapter.model.PracticePlanGenerateResponse;
import com.hbwj.domain.useCase.PracticePlanGenerationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/practice-plans/generator")
@Tag(name = "Practice Plan Generator", description = "Practice Plan Generator API")
public class PracticePlanGeneratorController {

    private final PracticePlanGenerationUseCase practicePlanGeneratorUseCase;

    @Autowired
    public PracticePlanGeneratorController(PracticePlanGenerationUseCase practicePlanGeneratorUseCase) {
        this.practicePlanGeneratorUseCase = practicePlanGeneratorUseCase;
    }

    @PostMapping("/preview")
    @Operation(summary = "Generate a practice plan preview without saving it")
    public ResponseEntity<PracticePlanGenerateResponse> generatePracticePlanPreview(
            @Valid @RequestBody PracticePlanGenerateRequest request) {
        PracticePlanGenerateResponse response = practicePlanGeneratorUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

//    @PostMapping
//    @Operation(summary = "Generate and save a practice plan")
//    public ResponseEntity<PracticePlanGenerateResponse> generateAndSavePracticePlan(
//            @Valid @RequestBody PracticePlanGenerateRequest requestDto) {
//        PracticePlanGenerateResponse planDto = practicePlanGeneratorUseCase.generateAndSavePracticePlan(requestDto);
//        return new ResponseEntity<>(planDto, HttpStatus.CREATED);
//    }
}
