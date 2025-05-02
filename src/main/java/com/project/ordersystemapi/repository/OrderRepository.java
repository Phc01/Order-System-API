package com.project.ordersystemapi.repository;

import com.project.ordersystemapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
