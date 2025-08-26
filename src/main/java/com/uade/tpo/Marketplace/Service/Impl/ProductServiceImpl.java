package com.uade.tpo.Marketplace.Service.Impl;



import com.uade.tpo.Marketplace.DTOs.ProductDetailDTO;
import com.uade.tpo.Marketplace.DTOs.ProductListDTO;
import com.uade.tpo.Marketplace.DTOs.Mapper.ProductMapper;
import com.uade.tpo.Marketplace.Entity.Product;
import com.uade.tpo.Marketplace.Repository.ProductRepository;
import com.uade.tpo.Marketplace.Service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private  ProductRepository productRepository;

    @Override
    public List<ProductListDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toSimpleDTO)
                .toList();
    }

    @Override
    public ProductDetailDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ProductMapper.toDetailDTO(product);
    }

    @Override
    public List<ProductListDTO> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(ProductMapper::toSimpleDTO)
                .toList();
    }

    @Override
    public List<ProductListDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(ProductMapper::toSimpleDTO)
                .toList();
    }
}

