package com.uade.tpo.Marketplace.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductListDTO {
    private Long id;
    private String name;
    private Double finalPrice;
    private String categoryName;
    private String mainImageUrl;
}
