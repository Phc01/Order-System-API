package com.project.ordersystemapi.repository;

import com.project.ordersystemapi.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
