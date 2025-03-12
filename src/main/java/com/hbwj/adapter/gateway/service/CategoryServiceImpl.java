package com.hbwj.adapter.gateway.service;

import com.hbwj.adapter.gateway.repository.CategoryRepository;
import com.hbwj.adapter.gateway.repository.FocusAreaRepository;
import com.hbwj.adapter.gateway.repository.SportRepository;
import com.hbwj.adapter.gateway.repository.entity.CategoryEntity;
import com.hbwj.adapter.gateway.repository.entity.FocusAreaEntity;
import com.hbwj.adapter.gateway.repository.entity.SportEntity;
import com.hbwj.domain.model.Category;
import com.hbwj.domain.port.CategoryService;
import com.hbwj.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final SportRepository sportRepository;
    private final FocusAreaRepository focusAreaRepository;

    @Autowired
    public CategoryServiceImpl(
            CategoryRepository categoryRepository,
            SportRepository sportRepository,
            FocusAreaRepository focusAreaRepository) {
        this.categoryRepository = categoryRepository;
        this.sportRepository = sportRepository;
        this.focusAreaRepository = focusAreaRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> getCategoriesBySportId(Long sportId) {
        return categoryRepository.findBySportId(sportId).stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> getCategoriesByFocusAreaId(Long focusAreaId) {
        return categoryRepository.findByFocusAreaId(focusAreaId).stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> getCategoriesBySportIdAndFocusAreaId(Long sportId, Long focusAreaId) {
        return categoryRepository.findBySportIdAndFocusAreaId(sportId, focusAreaId).stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public Category getCategoryById(Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return mapTo(category);
    }

    @Override
    @Transactional
    public Category createCategory(Category domain) {
        SportEntity sport = sportRepository.findById(domain.getSportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport not found with id: " + domain.getSportId()));

        FocusAreaEntity focusArea = focusAreaRepository.findById(domain.getFocusAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Focus Area not found with id: " + domain.getFocusAreaId()));

        CategoryEntity category = new CategoryEntity();
        category.setName(category.getName());
        category.setDescription(category.getDescription());
        category.setSport(sport);
        category.setFocusArea(focusArea);

        CategoryEntity savedCategory = categoryRepository.save(category);
        return mapTo(savedCategory);
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, Category domain) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        SportEntity sport = sportRepository.findById(domain.getSportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport not found with id: " + domain.getSportId()));

        FocusAreaEntity focusArea = focusAreaRepository.findById(domain.getFocusAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Focus Area not found with id: " + domain.getFocusAreaId()));

        category.setName(category.getName());
        category.setDescription(category.getDescription());
        category.setSport(sport);
        category.setFocusArea(focusArea);

        CategoryEntity updatedCategory = categoryRepository.save(category);
        return mapTo(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    private Category mapTo(CategoryEntity category) {
        Category dto = new Category();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setSportId(category.getSport().getId());
        dto.setSportName(category.getSport().getName());
        dto.setFocusAreaId(category.getFocusArea().getId());
        dto.setFocusAreaName(category.getFocusArea().getName());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }
}
