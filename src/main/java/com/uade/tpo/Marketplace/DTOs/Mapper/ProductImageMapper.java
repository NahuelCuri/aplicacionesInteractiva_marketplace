package com.uade.tpo.Marketplace.DTOs.Mapper;

import com.uade.tpo.Marketplace.DTOs.ProductImageDTO;
import com.uade.tpo.Marketplace.Entity.ProductImage;

public class ProductImageMapper {

    private ProductImageMapper() {}

    public static ProductImageDTO toProductImageDTO(ProductImage productImage) {
        ProductImageDTO productImageDTO = new ProductImageDTO();
        productImageDTO.setId(productImage.getId());
        return productImageDTO;
    }
}