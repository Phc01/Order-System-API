package com.project.ordersystemapi.service;

import com.project.ordersystemapi.dto.CreateUserDTO;
import com.project.ordersystemapi.dto.UpdateUserDTO;
import com.project.ordersystemapi.dto.UserResponseDTO;
import com.project.ordersystemapi.exception.ResourceNotFoundException;
import com.project.ordersystemapi.model.User;
import com.project.ordersystemapi.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected UserResponseDTO convertToDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()
                );
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(CreateUserDTO createUserDTO) {
        User user = new User();
        user.setName(createUserDTO.getName());
        user.setEmail(createUserDTO.getEmail());

        if (userRepository.existsByEmail(createUserDTO.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists: "
                    + createUserDTO.getEmail());
        }

        User savedUser = userRepository.save(user);

        return convertToDTO(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UpdateUserDTO updateUserDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        boolean updated = false;

        if (updateUserDTO.getName() != null && !updateUserDTO.getName()
                .equals(existingUser.getName())) {
            existingUser.setName(updateUserDTO.getName());
            updated = true;
        }

        if (updateUserDTO.getEmail() != null && !updateUserDTO.getEmail()
                .equals(existingUser.getEmail())) {
            if (userRepository.existsByEmailAndIdNot(updateUserDTO.getEmail(), id)) {
                throw new DataIntegrityViolationException("Email already exists for another user: "
                        + updateUserDTO.getEmail());
            }
            existingUser.setEmail(updateUserDTO.getEmail());
            updated = true;
        }

        if (updated) {
            User updatedUser = userRepository.save(existingUser);
            return convertToDTO(updatedUser);
        }

        return convertToDTO(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }
}
