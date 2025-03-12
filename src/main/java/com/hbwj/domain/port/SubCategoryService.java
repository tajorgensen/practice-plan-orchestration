package com.hbwj.domain.port;

import com.hbwj.domain.model.SubCategory;

import java.util.List;

public interface SubCategoryService {
    List<SubCategory> getAllSubCategories();

    List<SubCategory> getSubCategoriesByCategoryId(Long categoryId);

    SubCategory getSubCategoryById(Long id);

    SubCategory createSubCategory(SubCategory subCategory);

    SubCategory updateSubCategory(Long id, SubCategory subCategory);

    void deleteSubCategory(Long id);
}
