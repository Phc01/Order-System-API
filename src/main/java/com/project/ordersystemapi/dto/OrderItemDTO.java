package com.project.ordersystemapi.dto;

public class OrderItemDTO {

    private Long productId;

    private Integer quantity;

    // Constructors, getters, and setters

    public OrderItemDTO(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
