package com.project.ordersystemapi.service;

import com.project.ordersystemapi.dto.CreateOrderDTO;
import com.project.ordersystemapi.dto.OrderItemResponseDTO;
import com.project.ordersystemapi.dto.OrderResponseDTO;
import com.project.ordersystemapi.exception.InsufficientStockException;
import com.project.ordersystemapi.exception.ResourceNotFoundException;
import com.project.ordersystemapi.model.*;
import com.project.ordersystemapi.repository.OrderRepository;
import com.project.ordersystemapi.repository.ProductRepository;
import com.project.ordersystemapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    private OrderResponseDTO convertToDTO(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderItemResponseDTO> itemDTOs = order.getItems()
                .stream()
                .map(item -> {
                    BigDecimal price = item.getPrice();
                    Integer quantity = item.getQuantity();
                    BigDecimal subtotal;

                    if (price != null && quantity != null) {
                        subtotal = price.multiply(new BigDecimal(quantity));
                    } else {
                        subtotal = BigDecimal.ZERO;
                    }

                    return new OrderItemResponseDTO(
                            item.getProduct() != null ? item.getProduct().getName() : "Product not available",
                            quantity,
                            price,
                            subtotal
                    );
                }).collect(Collectors.toList());

        BigDecimal total = itemDTOs
                .stream()
                .map(OrderItemResponseDTO::getSubtotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrderResponseDTO(
                order.getId(),
                order.getMoment(),
                order.getStatus() != null ? order.getStatus().name() : "Status not defined",
                order.getUser() != null ? order.getUser().getName() : "User not defined",
                itemDTOs,
                total.setScale(2, RoundingMode.HALF_UP).doubleValue()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrderByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: "
                    + userId + ", cannot fetch orders.");
        }

        List<Order> orders = orderRepository.findByUserId(userId);
        return orders
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponseDTO createOrder(CreateOrderDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + dto.getUserId()));

        Order order = new Order();
        order.setUser(user);
        order.setMoment(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = dto.getItems()
                .stream()
                .map(orderItemDTO -> {
                    Product product = productRepository.findById(orderItemDTO.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + orderItemDTO.getProductId()));

                    if (product.getStock() < orderItemDTO.getQuantity()) {
                        throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
                    }
                    product.setStock(product.getStock() - orderItemDTO.getQuantity());
                    productRepository.save(product);

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProduct(product);
                    orderItem.setQuantity(orderItemDTO.getQuantity());
                    orderItem.setPrice(product.getPrice());
                    return orderItem;
                }).collect(Collectors.toList());

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }
}
