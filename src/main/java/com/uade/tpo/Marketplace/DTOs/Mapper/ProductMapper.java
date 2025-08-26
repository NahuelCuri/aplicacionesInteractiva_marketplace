package com.uade.tpo.Marketplace.DTOs.Mapper;

import com.uade.tpo.Marketplace.DTOs.ProductDetailDTO;
import com.uade.tpo.Marketplace.DTOs.ProductListDTO;
import com.uade.tpo.Marketplace.Entity.Product;
import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductListDTO toSimpleDTO(Product product) {
        String mainImage = null;
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            mainImage = product.getImages().get(0).getUrl();
        }

        double finalPrice = calcFinalPrice(product);

        return new ProductListDTO(
                product.getId(),
                product.getName(),
                finalPrice,
                product.getCategory() != null ? product.getCategory().getName() : null,
                mainImage
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
                        ? product.getImages().stream().map(img -> img.getUrl()).collect(Collectors.toList())
                        : null,
                product.getStock(),
                product.getDiscountPercentage()
        );
    }

    private static double calcFinalPrice(Product product) {
        double finalPrice = product.getPrice();
        if (product.getDiscountPercentage() != null && product.getDiscountPercentage() > 0) {
            finalPrice = finalPrice - (finalPrice * product.getDiscountPercentage() / 100);
        }
        return finalPrice;
    }
}

