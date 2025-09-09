package com.uade.tpo.Marketplace.Service.Impl; 

import com.uade.tpo.Marketplace.DTOs.Mapper.OrderMapper;
import com.uade.tpo.Marketplace.DTOs.OrderItemRequestDTO;
import com.uade.tpo.Marketplace.DTOs.OrderRequestDTO;
import com.uade.tpo.Marketplace.DTOs.OrderResponseDTO;
import com.uade.tpo.Marketplace.Entity.*;
import com.uade.tpo.Marketplace.Repository.OrderRepository;
import com.uade.tpo.Marketplace.Repository.ProductRepository;
import com.uade.tpo.Marketplace.Service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO, User buyer) {
        Order order = new Order();
        order.setBuyer(buyer);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setItems(new ArrayList<>());

        double totalPrice = 0.0;

        for (OrderItemRequestDTO itemRequest : orderRequestDTO.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + itemRequest.getProductId()));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());
            orderItem.setOrder(order);
            order.getItems().add(orderItem);

            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);

            totalPrice += (product.getPrice() * itemRequest.getQuantity());
        }

        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponseDTO(savedOrder);
    }

    @Override
    public Optional<OrderResponseDTO> getOrderByIdAndBuyerId(Long orderId, Long buyerId) {
        return orderRepository.findByIdAndBuyerId(orderId, buyerId)
                .map(orderMapper::toOrderResponseDTO);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByBuyerId(Long buyerId) {
        return orderRepository.findByBuyerId(buyerId).stream()
                .map(orderMapper::toOrderResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId, Long buyerId) {
        Order order = orderRepository.findByIdAndBuyerId(orderId, buyerId)
                .orElseThrow(() -> new RuntimeException("Order not found or access denied"));

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        orderRepository.delete(order);
    }
}