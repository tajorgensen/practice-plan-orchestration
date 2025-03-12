package com.hbwj.adapter.api;

import com.hbwj.domain.model.SubCategory;
import com.hbwj.domain.port.SubCategoryService;
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
@RequestMapping("/api/subcategories")
@Tag(name = "SubCategories", description = "SubCategories Management API")
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @Autowired
    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @GetMapping
    @Operation(summary = "Get all subcategories")
    public ResponseEntity<List<SubCategory>> getAllSubCategories() {
        return ResponseEntity.ok(subCategoryService.getAllSubCategories());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a subcategory by ID")
    public ResponseEntity<SubCategory> getSubCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(subCategoryService.getSubCategoryById(id));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get subcategories by category ID")
    public ResponseEntity<List<SubCategory>> getSubCategoriesByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(subCategoryService.getSubCategoriesByCategoryId(categoryId));
    }

    @PostMapping
    @Operation(summary = "Create a new subcategory")
    public ResponseEntity<SubCategory> createSubCategory(@Valid @RequestBody SubCategory subcategoryDto) {
        return new ResponseEntity<>(subCategoryService.createSubCategory(subcategoryDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a subcategory")
    public ResponseEntity<SubCategory> updateSubCategory(@PathVariable Long id, @Valid @RequestBody SubCategory subcategoryDto) {
        return ResponseEntity.ok(subCategoryService.updateSubCategory(id, subcategoryDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a subcategory")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable Long id) {
        subCategoryService.deleteSubCategory(id);
        return ResponseEntity.noContent().build();
    }
}
