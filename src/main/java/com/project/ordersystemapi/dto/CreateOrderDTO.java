package com.project.ordersystemapi.dto;

import java.util.List;

public class CreateOrderDTO {

    private Long userId;

    private List<OrderItemDTO> items;

    // Getters and setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
}
