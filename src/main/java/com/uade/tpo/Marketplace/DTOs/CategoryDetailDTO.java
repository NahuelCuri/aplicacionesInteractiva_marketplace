package com.uade.tpo.Marketplace.DTOs;

import lombok.Data;
import java.util.List;

@Data
public class CategoryDetailDTO {
    private Long id;
    private String name;
    private String description;
    private List<ProductListDTO> products;
}