package com.uade.tpo.Marketplace.Service;

import com.uade.tpo.Marketplace.DTOs.CategoryDetailDTO;
import com.uade.tpo.Marketplace.DTOs.CategoryListDTO;
import com.uade.tpo.Marketplace.DTOs.CategoryRequestDTO;
import java.util.List;

public interface CategoryService {
    CategoryDetailDTO createCategory(CategoryRequestDTO categoryRequestDTO);

    CategoryDetailDTO getCategoryById(Long id);

    List<CategoryListDTO> getAllCategories();

    CategoryDetailDTO updateCategory(Long id, CategoryRequestDTO categoryRequestDTO);

    void deleteCategory(Long id);
}