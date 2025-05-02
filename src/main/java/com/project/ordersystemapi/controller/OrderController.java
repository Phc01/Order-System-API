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
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrderByUser(@PathVariable Long userId) {
        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> order.getUser().getId().equals(userId))
                .collect(Collectors.toList());

        return ResponseEntity.ok(orders);
    }

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
