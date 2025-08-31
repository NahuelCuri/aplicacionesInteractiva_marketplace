package com.uade.tpo.Marketplace.DTOs;

import lombok.Data;

@Data
public class OrderItemRequestDTO{
    private Long productId;
    private Integer quantity;
}