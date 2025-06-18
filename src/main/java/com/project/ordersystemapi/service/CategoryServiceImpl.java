package com.project.ordersystemapi.service;

import com.project.ordersystemapi.dto.CategoryResponseDTO;
import com.project.ordersystemapi.dto.CreateCategoryDTO;
import com.project.ordersystemapi.dto.UpdateCategoryDTO;
import com.project.ordersystemapi.exception.ResourceNotFoundException;
import com.project.ordersystemapi.model.Category;
import com.project.ordersystemapi.repository.CategoryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    protected CategoryResponseDTO convertToDTO(Category category) {
        if (category == null) {
            return null;
        }

        return new CategoryResponseDTO(category.getId(), category.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()
                );
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CreateCategoryDTO createCategoryDTO) {
        Category category = new Category();
        category.setName(createCategoryDTO.getName());

        if (categoryRepository.existsByName(createCategoryDTO.getName())) {
            throw new ResourceNotFoundException("Category already exists with name: "
                    + createCategoryDTO.getName());
        }

        Category savedCategory = categoryRepository.save(category);

        return convertToDTO(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategory(Long id, UpdateCategoryDTO updateCategoryDTO) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        boolean updated = false;
        if (updateCategoryDTO.getName() != null && !updateCategoryDTO.getName()
                .equals(existingCategory.getName())) {
            if (categoryRepository.existsByName(updateCategoryDTO.getName())) {
                throw new DataIntegrityViolationException("Category already exists with name: "
                        + updateCategoryDTO.getName());
            }
            existingCategory.setName(updateCategoryDTO.getName());
            updated = true;
        }

        if (updated) {
            Category updatedCategory = categoryRepository.save(existingCategory);
            return convertToDTO(updatedCategory);
        }

        return convertToDTO(existingCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }

        categoryRepository.deleteById(id);
    }
}
