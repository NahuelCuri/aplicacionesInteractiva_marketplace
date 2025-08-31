package com.uade.tpo.Marketplace.DTOs;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequestDTO {
    private List<OrderItemRequestDTO> items;
}