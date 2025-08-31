package com.uade.tpo.Marketplace.Service;

import com.uade.tpo.Marketplace.Entity.ProductImage;

import java.util.List;

public interface ProductImageService {
    ProductImage addImageToProduct(Long productId, byte[] imageData);
    ProductImage getImageById(Long id);
    List<ProductImage> getImagesByProduct(Long productId);
    void deleteImage(Long id);
}
