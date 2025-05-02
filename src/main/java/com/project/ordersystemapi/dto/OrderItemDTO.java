package com.project.ordersystemapi.dto;

import lombok.Data;

@Data
public class OrderItemDTO {

    private Long productId;

    private Integer quantity;
}
