package com.uade.tpo.Marketplace.Controller;

import com.uade.tpo.Marketplace.DTOs.ProductImageDTO;
import com.uade.tpo.Marketplace.Entity.ProductImage;
import com.uade.tpo.Marketplace.Service.ProductImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product-images")
public class ProductImageController {

    @Autowired
    private  ProductImageService productImageService;


    public ResponseEntity<ProductImageDTO> addImageToProduct( @PathVariable Long productId, @RequestParam("image") MultipartFile imageFile) {
        try {
            ProductImageDTO savedImage = productImageService.addImageToProduct(productId, imageFile.getBytes());
            return ResponseEntity.ok(savedImage);
        } catch (java.io.IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImageById(@PathVariable Long id) {
        ProductImage productImage = productImageService.getImageById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) 
                .body(productImage.getImageData());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductImageDTO>> getImagesByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productImageService.getImagesByProduct(productId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        productImageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}