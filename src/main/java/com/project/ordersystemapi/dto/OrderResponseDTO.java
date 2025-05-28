package com.project.ordersystemapi.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {
    private Long id;
    private LocalDateTime moment;
    private String status;
    private String userName;
    private List<OrderItemResponseDTO> items;
    private Double total;

    // Constructor e Getters

    public OrderResponseDTO(Long id, LocalDateTime moment, String status, String userName, List<OrderItemResponseDTO> items, Double total) {
        this.id = id;
        this.moment = moment;
        this.status = status;
        this.userName = userName;
        this.items = items;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getMoment() {
        return moment;
    }

    public String getStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }

    public List<OrderItemResponseDTO> getItems() {
        return items;
    }

    public Double getTotal() {
        return total;
    }

}
