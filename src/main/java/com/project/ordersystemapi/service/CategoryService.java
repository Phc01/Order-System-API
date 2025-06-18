package com.project.ordersystemapi.service;

import com.project.ordersystemapi.dto.CategoryResponseDTO;
import com.project.ordersystemapi.dto.CreateCategoryDTO;
import com.project.ordersystemapi.dto.UpdateCategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDTO> getAllCategories();
    CategoryResponseDTO getCategoryById(Long id);
    CategoryResponseDTO createCategory(CreateCategoryDTO createCategoryDTO);
    CategoryResponseDTO updateCategory(Long id, UpdateCategoryDTO updateCategoryDTO);
    void deleteCategory(Long id);
}
