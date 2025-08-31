package com.uade.tpo.Marketplace.DTOs;



import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductCreateDTO {
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Double discountPercentage;
    private Long categoryId;
    private Long sellerId;
    private List<MultipartFile> images; 
}
