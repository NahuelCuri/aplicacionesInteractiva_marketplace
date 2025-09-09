package com.uade.tpo.Marketplace.Service;

import com.uade.tpo.Marketplace.DTOs.OrderRequestDTO;
import com.uade.tpo.Marketplace.DTOs.OrderResponseDTO;
import com.uade.tpo.Marketplace.Entity.User;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO, User buyer);
    Optional<OrderResponseDTO> getOrderByIdAndBuyerId(Long orderId, Long buyerId);
    List<OrderResponseDTO> getOrdersByBuyerId(Long buyerId);
    void deleteOrder(Long orderId, Long buyerId);
}