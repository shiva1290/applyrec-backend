package com.applyrec.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {

    public static class SignupRequest {
        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class LoginRequest {
        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class AuthResponse {
        private String message;
        private UserInfo user;
        private String token;

        public AuthResponse(String message, UserInfo user, String token) {
            this.message = message;
            this.user = user;
            this.token = token;
        }

        public String getMessage() {
            return message;
        }

        public UserInfo getUser() {
            return user;
        }

        public String getToken() {
            return token;
        }
    }

    public static class UserInfo {
        private Long id;
        private String email;

        public UserInfo(Long id, String email) {
            this.id = id;
            this.email = email;
        }

        public Long getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }
    }
}

