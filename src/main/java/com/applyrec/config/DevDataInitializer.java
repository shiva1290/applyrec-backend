package com.applyrec.config;

import com.applyrec.entity.Application;
import com.applyrec.entity.User;
import com.applyrec.repository.ApplicationRepository;
import com.applyrec.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
@Profile("dev")
public class DevDataInitializer {

    @Bean
    public CommandLineRunner seedDevData(
            UserRepository userRepository,
            ApplicationRepository applicationRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            String email = "test@applyrec.com";
            String rawPassword = "test123";

            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user = userRepository.save(user);

            List<Application> apps = List.of(
                    buildApp(user, "Acme Corp", "Software Engineer", "Applied", LocalDate.parse("2025-02-01")),
                    buildApp(user, "TechStart Inc", "Full Stack Developer", "OA", LocalDate.parse("2025-02-05")),
                    buildApp(user, "BigCo", "Backend Engineer", "Interview", LocalDate.parse("2025-01-20")),
                    buildApp(user, "StartupXYZ", "Frontend Developer", "Rejected", LocalDate.parse("2025-01-10")),
                    buildApp(user, "DreamJob Ltd", "Senior Engineer", "Offer", LocalDate.parse("2024-12-15"))
            );

            applicationRepository.saveAll(apps);
        };
    }

    private Application buildApp(User user, String company, String role, String status, LocalDate appliedDate) {
        Application app = new Application();
        app.setUser(user);
        app.setCompany(company);
        app.setRole(role);
        app.setStatus(status);
        app.setAppliedDate(appliedDate);
        return app;
    }
}

