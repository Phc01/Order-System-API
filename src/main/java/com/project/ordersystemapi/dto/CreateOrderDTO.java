package com.project.ordersystemapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDTO {

    private Long userId;

    private List<OrderItemDTO> items;
}
