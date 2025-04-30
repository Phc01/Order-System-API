package com.project.ordersystemapi.repository;

import com.project.ordersystemapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
