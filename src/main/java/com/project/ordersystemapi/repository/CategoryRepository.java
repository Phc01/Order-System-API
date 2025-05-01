package com.project.ordersystemapi.repository;

import com.project.ordersystemapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
