package com.uade.tpo.Marketplace.DTOs.Mapper; // Placing it in the same package as ProductMapper

import com.uade.tpo.Marketplace.DTOs.CategoryDetailDTO;
import com.uade.tpo.Marketplace.DTOs.CategoryListDTO;
import com.uade.tpo.Marketplace.Entity.Category;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CategoryMapper {

    public static CategoryListDTO toCategoryListDTO(Category category) {
        CategoryListDTO dto = new CategoryListDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }

    public static CategoryDetailDTO toCategoryDetailDTO(Category category) {
        CategoryDetailDTO dto = new CategoryDetailDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());

        if (category.getProducts() != null) {
            dto.setProducts(category.getProducts().stream()
                .map(ProductMapper::toSimpleDTO) 
                .collect(Collectors.toList()));
        } else {
            dto.setProducts(new ArrayList<>());
        }

        return dto;
    }
}