package com.uade.tpo.Marketplace.Controller;

import com.uade.tpo.Marketplace.DTOs.OrderRequestDTO;
import com.uade.tpo.Marketplace.DTOs.OrderResponseDTO;
import com.uade.tpo.Marketplace.Entity.User;
import com.uade.tpo.Marketplace.Repository.UserRepository;
import com.uade.tpo.Marketplace.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/cart")
    public ResponseEntity<OrderResponseDTO> getCart(@AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be logged in to view cart");
        }
        OrderResponseDTO cart = orderService.getCart(currentUser);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/cart/items")
    public ResponseEntity<OrderResponseDTO> addItemToCart(
            @AuthenticationPrincipal User currentUser,
            @RequestBody com.uade.tpo.Marketplace.DTOs.OrderItemRequestDTO itemRequest) {
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be logged in to add items to cart");
        }
        OrderResponseDTO updatedCart = orderService.addItemToCart(currentUser, itemRequest);
        return ResponseEntity.ok(updatedCart);
    }

    @PostMapping("/users/{buyerId}/orders")
    public ResponseEntity<OrderResponseDTO> createOrder(
            @PathVariable Long buyerId,
            @RequestBody OrderRequestDTO orderRequest) {
        
        User currentUser = userRepository.findById(buyerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + buyerId));

        OrderResponseDTO createdOrder = orderService.createOrder(orderRequest, currentUser);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }


    @GetMapping("/users/{buyerId}/orders")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUser(@PathVariable Long buyerId) {
        if (!userRepository.existsById(buyerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + buyerId);
        }
        
        List<OrderResponseDTO> orders = orderService.getOrdersByBuyerId(buyerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/users/{buyerId}/orders/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @PathVariable Long buyerId,
            @PathVariable Long orderId) {

        
        OrderResponseDTO order = orderService.getOrderByIdAndBuyerId(orderId, buyerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found or access denied"));
        
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/users/{buyerId}/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable Long buyerId,
            @PathVariable Long orderId) {
        orderService.deleteOrder(orderId, buyerId);
        return ResponseEntity.noContent().build();
    }
}