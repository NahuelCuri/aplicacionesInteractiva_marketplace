package com.uade.tpo.Marketplace.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Double finalPrice;
    private String categoryName;
    private String sellerUsername;
    private List<String> images;
    private Integer stock;
    private Double discountPercentage;
}
