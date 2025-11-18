package com.example.userbackend.service;

import com.example.userbackend.dto.UserRequestDto;
import com.example.userbackend.dto.UserResponseDto;
import com.example.userbackend.model.User;
import com.example.userbackend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UserResponseDto createUser(UserRequestDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already registered");
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhoneNumber(dto.getPhoneNumber());
        User saved = userRepository.save(user);
        return toDto(saved);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        return toDto(user);
    }

    public UserResponseDto updateUser(Long id, UserRequestDto dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        User updated = userRepository.save(user);
        return toDto(updated);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found");
        }
        userRepository.deleteById(id);
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getPhoneNumber());
    }
}
