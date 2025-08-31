package com.uade.tpo.Marketplace.DTOs.Mapper;

import com.uade.tpo.Marketplace.DTOs.OrderItemResponseDTO;
import com.uade.tpo.Marketplace.DTOs.OrderResponseDTO;
import com.uade.tpo.Marketplace.Entity.Order;
import com.uade.tpo.Marketplace.Entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponseDTO toOrderResponseDTO(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());

        if (order.getBuyer() != null) {
            dto.setBuyerId(order.getBuyer().getId());
        }

        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                    .map(this::toOrderItemResponseDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setItems(Collections.emptyList());
        }

        return dto;
    }

    private OrderItemResponseDTO toOrderItemResponseDTO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setQuantity(orderItem.getQuantity());
        dto.setPriceAtPurchase(orderItem.getPriceAtPurchase());

        if (orderItem.getProduct() != null) {
            dto.setProductId(orderItem.getProduct().getId());
            dto.setProduct(ProductMapper.toSimpleDTO(orderItem.getProduct()));
        }

        return dto;
    }
}