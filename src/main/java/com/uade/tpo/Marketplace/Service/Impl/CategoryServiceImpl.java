package com.uade.tpo.Marketplace.Service.Impl;

import com.uade.tpo.Marketplace.DTOs.CategoryDetailDTO;
import com.uade.tpo.Marketplace.DTOs.CategoryListDTO;
import com.uade.tpo.Marketplace.DTOs.CategoryRequestDTO;
import com.uade.tpo.Marketplace.Entity.Category;
import com.uade.tpo.Marketplace.DTOs.Mapper.CategoryMapper;
import com.uade.tpo.Marketplace.Repository.CategoryRepository;
import com.uade.tpo.Marketplace.Service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryDetailDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = new Category();
        category.setName(categoryRequestDTO.getName());
        category.setDescription(categoryRequestDTO.getDescription());
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.toCategoryDetailDTO(savedCategory);
    }

    @Override
    public CategoryDetailDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return CategoryMapper.toCategoryDetailDTO(category);
    }

    @Override
    public List<CategoryListDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::toCategoryListDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDetailDTO updateCategory(Long id, CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        category.setName(categoryRequestDTO.getName());
        category.setDescription(categoryRequestDTO.getDescription());
        Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.toCategoryDetailDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}