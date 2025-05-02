package com.project.ordersystemapi.controller;

import com.project.ordersystemapi.dto.CreateOrderDTO;
import com.project.ordersystemapi.model.Order;
import com.project.ordersystemapi.model.OrderItem;
import com.project.ordersystemapi.model.OrderStatus;
import com.project.ordersystemapi.repository.OrderRepository;
import com.project.ordersystemapi.repository.ProductRepository;
import com.project.ordersystemapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.source.spi.Orderable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderDTO dto) {
        var user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var order = Order.builder()
                .user(user)
                .moment(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        List<OrderItem> items = dto.getItems().stream().map(itemDto -> {
            var product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            return OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .price(product.getPrice())
                    .build();
        }).collect(Collectors.toList());

        order.setItems(items);

        var savedOrder = orderRepository.save(order);
        return ResponseEntity.ok(savedOrder);
    }
}
