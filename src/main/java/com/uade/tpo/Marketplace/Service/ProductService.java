package com.uade.tpo.Marketplace.Service;

import com.uade.tpo.Marketplace.DTOs.ProductDetailDTO;
import com.uade.tpo.Marketplace.DTOs.ProductListDTO;

import java.util.List;


public interface ProductService {
    List<ProductListDTO> getAllProducts();
    ProductDetailDTO getProductById(Long id);
    List<ProductListDTO> searchProductsByName(String name);
    List<ProductListDTO> getProductsByCategory(Long categoryId);
}

