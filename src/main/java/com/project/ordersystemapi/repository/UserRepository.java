package com.project.ordersystemapi.repository;

import com.project.ordersystemapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
