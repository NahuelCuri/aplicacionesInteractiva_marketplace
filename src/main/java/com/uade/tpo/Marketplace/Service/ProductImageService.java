package com.uade.tpo.Marketplace.Service;

import com.uade.tpo.Marketplace.DTOs.ProductImageDTO;
import com.uade.tpo.Marketplace.Entity.ProductImage;
import java.util.List;

public interface ProductImageService {
    ProductImageDTO addImageToProduct(Long productId, byte[] imageData);
    ProductImage getImageById(Long id);
    List<ProductImageDTO> getImagesByProduct(Long productId);
    void deleteImage(Long id);
}
