package com.hbwj.domain.port;

import com.hbwj.domain.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    List<Category> getCategoriesBySportId(Long sportId);

    List<Category> getCategoriesByFocusAreaId(Long focusAreaId);

    List<Category> getCategoriesBySportIdAndFocusAreaId(Long sportId, Long focusAreaId);

    Category getCategoryById(Long id);

    Category createCategory(Category category);

    Category updateCategory(Long id, Category category);

    void deleteCategory(Long id);
}
