package com.uade.tpo.Marketplace.Service.Impl;

import com.uade.tpo.Marketplace.DTOs.Mapper.ProductImageMapper;
import com.uade.tpo.Marketplace.DTOs.ProductImageDTO;
import com.uade.tpo.Marketplace.Entity.Product;
import com.uade.tpo.Marketplace.Entity.ProductImage;
import com.uade.tpo.Marketplace.Repository.ProductImageRepository;
import com.uade.tpo.Marketplace.Repository.ProductRepository;
import com.uade.tpo.Marketplace.Service.ProductImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    public ProductImageDTO addImageToProduct(Long productId, byte[] imageData) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id " + productId));

        ProductImage productImage = new ProductImage();
        productImage.setImageData(imageData);
        productImage.setProduct(product);

        ProductImage savedImage = productImageRepository.save(productImage);
        return ProductImageMapper.toProductImageDTO(savedImage); 
    }

    @Override
    public ProductImage getImageById(Long id) {
        return productImageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Image not found with id " + id));
    }

    @Override
    public List<ProductImageDTO> getImagesByProduct(Long productId) {
        return productImageRepository.findByProductId(productId).stream()
                .map(ProductImageMapper::toProductImageDTO) 
                .collect(Collectors.toList());
    }

    @Override
    public void deleteImage(Long id) {
        if (!productImageRepository.existsById(id)) {
            throw new NoSuchElementException("Image not found with id " + id);
        }
        productImageRepository.deleteById(id);
    }
}