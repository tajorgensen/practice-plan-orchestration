package com.hbwj.adapter.api;

import com.hbwj.domain.model.Category;
import com.hbwj.domain.port.CategoryService;
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
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Categories Management API")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Get all categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a category by ID")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/sport/{sportId}")
    @Operation(summary = "Get categories by sport ID")
    public ResponseEntity<List<Category>> getCategoriesBySportId(@PathVariable Long sportId) {
        return ResponseEntity.ok(categoryService.getCategoriesBySportId(sportId));
    }

    @GetMapping("/focus-area/{focusAreaId}")
    @Operation(summary = "Get categories by focus area ID")
    public ResponseEntity<List<Category>> getCategoriesByFocusAreaId(@PathVariable Long focusAreaId) {
        return ResponseEntity.ok(categoryService.getCategoriesByFocusAreaId(focusAreaId));
    }

    @GetMapping("/sport/{sportId}/focus-area/{focusAreaId}")
    @Operation(summary = "Get categories by sport ID and focus area ID")
    public ResponseEntity<List<Category>> getCategoriesBySportIdAndFocusAreaId(
            @PathVariable Long sportId, @PathVariable Long focusAreaId) {
        return ResponseEntity.ok(categoryService.getCategoriesBySportIdAndFocusAreaId(sportId, focusAreaId));
    }

    @PostMapping
    @Operation(summary = "Create a new category")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category categoryDto) {
        return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody Category categoryDto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
