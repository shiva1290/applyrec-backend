package com.applyrec.controller;

import com.applyrec.dto.AuthDtos;
import com.applyrec.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody AuthDtos.SignupRequest request) {
        try {
            AuthDtos.AuthResponse response = authService.signup(request.getEmail(), request.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    java.util.Map.of("error", ex.getMessage())
            );
        } catch (IllegalStateException ex) {
            if ("Email already registered".equals(ex.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        java.util.Map.of("error", ex.getMessage())
                );
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    java.util.Map.of("error", "Internal server error")
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    java.util.Map.of("error", "Internal server error")
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDtos.LoginRequest request) {
        try {
            AuthDtos.AuthResponse response = authService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            String message = ex.getMessage();
            if ("Email and password are required".equals(message)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        java.util.Map.of("error", message)
                );
            }
            if ("Invalid email or password".equals(message)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        java.util.Map.of("error", message)
                );
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    java.util.Map.of("error", "Internal server error")
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    java.util.Map.of("error", "Internal server error")
            );
        }
    }
}

