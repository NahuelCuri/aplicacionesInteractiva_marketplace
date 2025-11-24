package com.uade.tpo.Marketplace.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductListDTO {
    private Long id;
    private String name;
    private Double finalPrice;
    private String categoryName;
    private Long categoryId;
    private List<Long> imageIds;
    private boolean deleted;
    private Integer stock;
}
