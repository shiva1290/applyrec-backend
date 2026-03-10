package com.applyrec.service;

import com.applyrec.dto.ApplicationDtos;
import com.applyrec.entity.Application;
import com.applyrec.entity.User;
import com.applyrec.repository.ApplicationRepository;
import com.applyrec.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class ApplicationService {

    private static final List<String> VALID_STATUSES =
            List.of("Applied", "OA", "Interview", "Rejected", "Offer");

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    private void validateStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new IllegalArgumentException("Invalid status");
        }
    }

    private void validateApplicationData(ApplicationDtos.CreateOrUpdateRequest request) {
        if (request.getCompany() == null || request.getCompany().isBlank()
                || request.getRole() == null || request.getRole().isBlank()
                || request.getStatus() == null || request.getStatus().isBlank()
                || request.getAppliedDate() == null) {
            throw new IllegalArgumentException("All fields are required");
        }
        validateStatus(request.getStatus());
    }

    @Transactional
    public Long createApplication(Long userId, ApplicationDtos.CreateOrUpdateRequest request) {
        validateApplicationData(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        Application app = new Application();
        app.setUser(user);
        app.setCompany(request.getCompany());
        app.setRole(request.getRole());
        app.setStatus(request.getStatus());
        app.setAppliedDate(request.getAppliedDate());
        app.setNotes(request.getNotes());
        app.setFollowUp(Boolean.TRUE.equals(request.getFollowUp()));
        app.setJobId(request.getJobId());
        app.setSalary(request.getSalary());

        Application saved = applicationRepository.save(app);
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public List<Application> getApplications(Long userId, String status, String role,
                                             BigDecimal minSalary, BigDecimal maxSalary) {
        if (status != null && !status.isBlank()) {
            validateStatus(status);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));
        return applicationRepository.findByUserWithFilters(
                user, status, role, minSalary, maxSalary
        );
    }

    @Transactional(readOnly = true)
    public Application getApplication(Long userId, Long id) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));
        return applicationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));
    }

    @Transactional
    public void updateApplication(Long userId, Long id, ApplicationDtos.CreateOrUpdateRequest request) {
        validateApplicationData(request);

        Application existing = getApplication(userId, id);
        String oldStatus = existing.getStatus();

        existing.setCompany(request.getCompany());
        existing.setRole(request.getRole());
        existing.setStatus(request.getStatus());
        existing.setAppliedDate(request.getAppliedDate());
        existing.setNotes(request.getNotes());
        existing.setFollowUp(Boolean.TRUE.equals(request.getFollowUp()));
        existing.setJobId(request.getJobId());
        existing.setSalary(request.getSalary());

        if (!oldStatus.equals(request.getStatus())) {
            existing.setStatusUpdatedAt(Instant.now());
        }

        applicationRepository.save(existing);
    }

    @Transactional
    public void updateApplicationStatus(Long userId, Long id, String status) {
        validateStatus(status);
        Application existing = getApplication(userId, id);
        existing.setStatus(status);
        existing.setStatusUpdatedAt(Instant.now());
        applicationRepository.save(existing);
    }

    @Transactional
    public void deleteApplication(Long userId, Long id) {
        Application existing = getApplication(userId, id);
        applicationRepository.delete(existing);
    }

    @Transactional(readOnly = true)
    public List<String> getUniqueRoles(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));
        return applicationRepository.findDistinctRolesByUser(user);
    }
}

