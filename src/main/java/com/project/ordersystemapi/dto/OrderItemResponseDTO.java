package com.project.ordersystemapi.dto;

import java.math.BigDecimal;

public class OrderItemResponseDTO {
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;

    public OrderItemResponseDTO(String productName, Integer quantity, BigDecimal price, BigDecimal subtotal) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
    }

    // Getters and setters

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
