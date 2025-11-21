package com.uade.tpo.Marketplace.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.uade.tpo.Marketplace.DTOs.ProductImageDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Double finalPrice;
    private Long categoryId;
    private String categoryName;
    private String sellerUsername;
    private List<ProductImageDTO> images;
    private Integer stock;
    private Double discountPercentage;
}
