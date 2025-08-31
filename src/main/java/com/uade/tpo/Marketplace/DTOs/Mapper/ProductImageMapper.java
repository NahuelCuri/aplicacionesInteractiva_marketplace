package com.uade.tpo.Marketplace.DTOs.Mapper;

import com.uade.tpo.Marketplace.DTOs.ProductImageInfoDTO;
import com.uade.tpo.Marketplace.Entity.ProductImage;

public class ProductImageMapper {

    private ProductImageMapper() {}

    public static ProductImageInfoDTO toProductImageInfoDTO(ProductImage productImage) {
        ProductImageInfoDTO dto = new ProductImageInfoDTO();
        dto.setId(productImage.getId());
        return dto;
    }
}