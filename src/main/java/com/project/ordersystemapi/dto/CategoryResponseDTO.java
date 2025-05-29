package com.project.ordersystemapi.dto;

public class CategoryResponseDTO {
    private Long id;
    private String name;

    // Constructors, getters, and setters
    public CategoryResponseDTO() {
    }

    public CategoryResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
