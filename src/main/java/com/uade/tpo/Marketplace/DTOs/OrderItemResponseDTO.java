package com.uade.tpo.Marketplace.DTOs;

import lombok.Data;

@Data
public class OrderItemResponseDTO {
    private Long productId;
    private ProductListDTO product;
    private Integer quantity;
    private Double priceAtPurchase;
}