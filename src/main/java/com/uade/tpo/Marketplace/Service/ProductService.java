package com.uade.tpo.Marketplace.Service;

import com.uade.tpo.Marketplace.DTOs.ProductCreateDTO;
import com.uade.tpo.Marketplace.DTOs.ProductDetailDTO;
import com.uade.tpo.Marketplace.DTOs.ProductListDTO;
import com.uade.tpo.Marketplace.DTOs.ProductUpdateDTO;

import java.util.List;


public interface ProductService {
    List<ProductListDTO> getAllProducts();
    ProductDetailDTO getProductById(Long id);
    List<ProductListDTO> searchProductsByName(String name);
    List<ProductListDTO> getProductsByCategory(Long categoryId);
    ProductDetailDTO createProduct(ProductCreateDTO dto);
    List<ProductListDTO> getProductsBySeller();
    List<ProductListDTO> searchProductsByNameAndSeller(String name);
    ProductDetailDTO updateProduct(Long id, ProductUpdateDTO productUpdateDTO);
}

