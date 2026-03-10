package com.applyrec.service;

import com.applyrec.dto.AuthDtos;
import com.applyrec.entity.User;
import com.applyrec.repository.UserRepository;
import com.applyrec.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthDtos.AuthResponse signup(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Email and password are required");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email already registered");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        User saved = userRepository.save(user);

        String token = jwtService.generateToken(saved.getId());
        AuthDtos.UserInfo userInfo = new AuthDtos.UserInfo(saved.getId(), saved.getEmail());
        return new AuthDtos.AuthResponse("User created successfully", userInfo, token);
    }

    public AuthDtos.AuthResponse login(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Email and password are required");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getId());
        AuthDtos.UserInfo userInfo = new AuthDtos.UserInfo(user.getId(), user.getEmail());
        return new AuthDtos.AuthResponse("Login successful", userInfo, token);
    }
}

