package com.project.ordersystemapi.service;

import com.project.ordersystemapi.dto.CreateOrderDTO;
import com.project.ordersystemapi.dto.OrderResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO getOrderById(Long id);

    List<OrderResponseDTO> getOrderByUser(Long userId);

    OrderResponseDTO createOrder(CreateOrderDTO dto);
}
