package com.project.ordersystemapi.service;

import com.project.ordersystemapi.dto.CreateUserDTO;
import com.project.ordersystemapi.dto.UpdateUserDTO;
import com.project.ordersystemapi.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    UserResponseDTO createUser(CreateUserDTO createUserDTO);
    UserResponseDTO updateUser(Long id, UpdateUserDTO updateUserDTO);
    void deleteUser(Long id);

}
