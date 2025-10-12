package com.uade.tpo.Marketplace.Repository;

import com.uade.tpo.Marketplace.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;



@Repository

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByBuyerId(Long buyerId);

    Optional<Order> findByIdAndBuyerId(Long orderId, Long buyerId);

    Optional<Order> findByBuyerIdAndStatus(Long buyerId, com.uade.tpo.Marketplace.Entity.OrderStatus status);

}
