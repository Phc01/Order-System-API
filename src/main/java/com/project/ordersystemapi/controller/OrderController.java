package com.project.ordersystemapi.controller;

import com.project.ordersystemapi.dto.CreateOrderDTO;
import com.project.ordersystemapi.dto.OrderItemResponseDTO;
import com.project.ordersystemapi.dto.OrderResponseDTO;
import com.project.ordersystemapi.model.Order;
import com.project.ordersystemapi.model.OrderItem;
import com.project.ordersystemapi.model.OrderStatus;
import com.project.ordersystemapi.model.User;
import com.project.ordersystemapi.model.Product;
import com.project.ordersystemapi.repository.OrderRepository;
import com.project.ordersystemapi.repository.ProductRepository;
import com.project.ordersystemapi.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    private OrderResponseDTO convertToOrderResponseDTO(Order order) {
        if (order == null) {
            return null;
        }
        List<OrderItemResponseDTO> itemDTOs = order.getItems().stream().map(item -> {
            BigDecimal price = item.getPrice();
            Integer quantity = item.getQuantity();
            BigDecimal subtotal;

            if (price != null && quantity != null) {
                subtotal = price.multiply(new BigDecimal(quantity));
            } else {
                subtotal = BigDecimal.ZERO;
            }

            return new OrderItemResponseDTO(
                    item.getProduct() != null ? item.getProduct().getName() : "Product not avaliable",
                    quantity,
                    price,
                    subtotal
            );
        }).collect(Collectors.toList());

        BigDecimal total = itemDTOs.stream()
                .map(OrderItemResponseDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrderResponseDTO(
                order.getId(),
                order.getMoment(),
                order.getStatus() != null ? order.getStatus().name() : "Status not defined",
                order.getUser() != null ? order.getUser().getName() : "User not defined",
                itemDTOs,
                total != null ? total.doubleValue() : 0.0
        );
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponseDTO> responseDTOs = orders.stream()
                .map(this::convertToOrderResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(this::convertToOrderResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrderByUser(@PathVariable Long userId) {
        List<Order> orders = orderRepository.findAll().stream()
                .filter(orderEntity -> orderEntity.getUser() != null && orderEntity.getUser().getId().equals(userId))
                        .collect(Collectors.toList());

        List<OrderResponseDTO> responseDTOs = orders.stream()
                .map(this::convertToOrderResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody CreateOrderDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));

        Order order = new Order();
        order.setUser(user);
        order.setMoment(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);


        List<OrderItem> items = dto.getItems().stream().map(itemDto -> {
            var product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(product.getPrice());

            return orderItem;
        }).collect(Collectors.toList());

        order.setItems(items);

        Order savedOrder = orderRepository.save(order);

        OrderResponseDTO responseDTO = convertToOrderResponseDTO(savedOrder);

        return  ResponseEntity.ok(responseDTO);
    }
}
