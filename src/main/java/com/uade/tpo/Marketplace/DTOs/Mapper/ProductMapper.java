package com.uade.tpo.Marketplace.DTOs.Mapper;

import com.uade.tpo.Marketplace.DTOs.ProductCreateDTO;
import com.uade.tpo.Marketplace.DTOs.ProductDetailDTO;
import com.uade.tpo.Marketplace.DTOs.ProductImageDTO;
import com.uade.tpo.Marketplace.DTOs.ProductListDTO;
import com.uade.tpo.Marketplace.Entity.Category;
import com.uade.tpo.Marketplace.Entity.Product;
import com.uade.tpo.Marketplace.Entity.ProductImage;
import com.uade.tpo.Marketplace.Entity.User;

import java.util.stream.Collectors;
import java.io.IOException;
import java.util.Base64;

public class ProductMapper {

    public static Product toEntity(ProductCreateDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setDiscountPercentage(dto.getDiscountPercentage());

        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            product.setCategory(category);
        }

        if (dto.getSellerId() != null) {
            User seller = new User();
            seller.setId(dto.getSellerId());
            product.setSeller(seller);
        }

        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            product.setImages(
                dto.getImages().stream().map(file -> {
                    try {
                        ProductImage img = new ProductImage();
                        img.setImageData(file.getBytes());
                        img.setProduct(product);
                        return img;
                    } catch (IOException e) {
                        throw new RuntimeException("Error al procesar imagen", e);
                    }
                }).collect(Collectors.toList())
            );
        }

        return product;
    }


    public static ProductListDTO toSimpleDTO(Product product) {
        String mainImageBase64 = null;
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            mainImageBase64 = Base64.getEncoder().encodeToString(product.getImages().get(0).getImageData());
        }

        double finalPrice = calcFinalPrice(product);

        return new ProductListDTO(
                product.getId(),
                product.getName(),
                finalPrice,
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getCategory() != null ? product.getCategory().getId() : null,
                mainImageBase64
        );
    }

    public static ProductDetailDTO toDetailDTO(Product product) {
        double finalPrice = calcFinalPrice(product);

        return new ProductDetailDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                finalPrice,
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getSeller() != null ? product.getSeller().getUsername() : null,
                product.getImages() != null
                        ? product.getImages().stream()
                                  .map(img -> {
                                      ProductImageDTO dto = new ProductImageDTO();
                                      dto.setId(img.getId());
                                      dto.setContent(Base64.getEncoder().encodeToString(img.getImageData()));
                                      return dto;
                                  })
                                  .collect(Collectors.toList())
                        : null,
                product.getStock(),
                product.getDiscountPercentage()
        );
    }

    private static double calcFinalPrice(Product product) {
        double finalPrice = product.getPrice();
        if (product.getDiscountPercentage() != null && product.getDiscountPercentage() > 0) {
            finalPrice -= (finalPrice * product.getDiscountPercentage() / 100);
        }
        return finalPrice;
    }
}


