package com.uade.tpo.Marketplace.Service.Impl;

import com.uade.tpo.Marketplace.Entity.Product;
import com.uade.tpo.Marketplace.Entity.ProductImage;
import com.uade.tpo.Marketplace.Repository.ProductImageRepository;
import com.uade.tpo.Marketplace.Repository.ProductRepository;
import com.uade.tpo.Marketplace.Service.ProductImageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    public ProductImageServiceImpl(ProductImageRepository productImageRepository, ProductRepository productRepository) {
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ProductImage addImageToProduct(Long productId, byte[] imageData) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id " + productId));

        ProductImage productImage = new ProductImage();
        productImage.setImageData(imageData);
        productImage.setProduct(product);

        return productImageRepository.save(productImage);
    }

    @Override
    public ProductImage getImageById(Long id) {
        return productImageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Image not found with id " + id));
    }

    @Override
    public List<ProductImage> getImagesByProduct(Long productId) {
        return productImageRepository.findByProductId(productId);
    }

    @Override
    public void deleteImage(Long id) {
        ProductImage existing = getImageById(id);
        productImageRepository.delete(existing);
    }
}
