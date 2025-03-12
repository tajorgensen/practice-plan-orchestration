package com.hbwj.adapter.gateway.service;

import com.hbwj.adapter.gateway.repository.CategoryRepository;
import com.hbwj.adapter.gateway.repository.SubCategoryRepository;
import com.hbwj.adapter.gateway.repository.entity.CategoryEntity;
import com.hbwj.adapter.gateway.repository.entity.SubCategoryEntity;
import com.hbwj.domain.model.SubCategory;
import com.hbwj.domain.port.SubCategoryService;
import com.hbwj.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public SubCategoryServiceImpl(
            SubCategoryRepository subcategoryRepository,
            CategoryRepository categoryRepository) {
        this.subcategoryRepository = subcategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<SubCategory> getAllSubCategories() {
        return subcategoryRepository.findAll().stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubCategory> getSubCategoriesByCategoryId(Long categoryId) {
        return subcategoryRepository.findByCategoryId(categoryId).stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public SubCategory getSubCategoryById(Long id) {
        SubCategoryEntity subcategory = subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found with id: " + id));
        return mapTo(subcategory);
    }

    @Override
    @Transactional
    public SubCategory createSubCategory(SubCategory domain) {
        CategoryEntity category = categoryRepository.findById(domain.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + domain.getCategoryId()));

        SubCategoryEntity subcategory = new SubCategoryEntity();
        subcategory.setName(subcategory.getName());
        subcategory.setDescription(subcategory.getDescription());
        subcategory.setCategory(category);

        SubCategoryEntity savedSubCategory = subcategoryRepository.save(subcategory);
        return mapTo(savedSubCategory);
    }

    @Override
    @Transactional
    public SubCategory updateSubCategory(Long id, SubCategory domain) {
        SubCategoryEntity subcategory = subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found with id: " + id));

        CategoryEntity category = categoryRepository.findById(domain.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + domain.getCategoryId()));

        subcategory.setName(subcategory.getName());
        subcategory.setDescription(subcategory.getDescription());
        subcategory.setCategory(category);

        SubCategoryEntity updatedSubCategory = subcategoryRepository.save(subcategory);
        return mapTo(updatedSubCategory);
    }

    @Override
    @Transactional
    public void deleteSubCategory(Long id) {
        if (!subcategoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("SubCategory not found with id: " + id);
        }
        subcategoryRepository.deleteById(id);
    }

    private SubCategory mapTo(SubCategoryEntity subcategory) {
        SubCategory dto = new SubCategory();
        dto.setId(subcategory.getId());
        dto.setName(subcategory.getName());
        dto.setDescription(subcategory.getDescription());
        dto.setCategoryId(subcategory.getCategory().getId());
        dto.setCategoryName(subcategory.getCategory().getName());
        dto.setSportName(subcategory.getCategory().getSport().getName());
        dto.setFocusAreaName(subcategory.getCategory().getFocusArea().getName());
        dto.setCreatedAt(subcategory.getCreatedAt());
        dto.setUpdatedAt(subcategory.getUpdatedAt());
        return dto;
    }
}
