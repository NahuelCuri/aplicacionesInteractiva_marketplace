package com.uade.tpo.Marketplace.Service;

import com.uade.tpo.Marketplace.DTOs.ProductImageInfoDTO;
import com.uade.tpo.Marketplace.Entity.ProductImage;
import java.util.List;

public interface ProductImageService {
    ProductImageInfoDTO addImageToProduct(Long productId, byte[] imageData);
    ProductImage getImageById(Long id);
    List<ProductImageInfoDTO> getImagesByProduct(Long productId);
    void deleteImage(Long id);
}
