package com.applyrec.controller;

import com.applyrec.dto.ApplicationDtos;
import com.applyrec.entity.Application;
import com.applyrec.security.UserPrincipal;
import com.applyrec.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<?> createApplication(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ApplicationDtos.CreateOrUpdateRequest request
    ) {
        try {
            Long id = applicationService.createApplication(principal.getUserId(), request);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "message", "Application created successfully",
                            "applicationId", id
                    )
            );
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            if ("All fields are required".equals(msg) || "Invalid status".equals(msg)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", msg));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Internal server error")
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Internal server error")
            );
        }
    }

    @GetMapping
    public ResponseEntity<?> getApplications(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Integer minSalary,
            @RequestParam(required = false) Integer maxSalary
    ) {
        try {
            BigDecimal min = minSalary != null ? BigDecimal.valueOf(minSalary) : null;
            BigDecimal max = maxSalary != null ? BigDecimal.valueOf(maxSalary) : null;
            List<Application> apps = applicationService.getApplications(
                    principal.getUserId(), status, role, min, max
            );
            List<ApplicationDtos.ApplicationResponse> responses = apps.stream()
                    .map(a -> new ApplicationDtos.ApplicationResponse(
                            a.getId(),
                            a.getJobId(),
                            a.getCompany(),
                            a.getRole(),
                            a.getStatus(),
                            a.getAppliedDate(),
                            a.getSalary(),
                            a.getNotes(),
                            a.isFollowUp(),
                            a.getStatusUpdatedAt()
                    ))
                    .toList();
            return ResponseEntity.ok(Map.of("applications", responses));
        } catch (IllegalArgumentException ex) {
            if ("Invalid status filter".equals(ex.getMessage()) || "Invalid status".equals(ex.getMessage())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Internal server error")
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Internal server error")
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getApplication(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        try {
            Application app = applicationService.getApplication(principal.getUserId(), id);
            ApplicationDtos.ApplicationResponse resp = new ApplicationDtos.ApplicationResponse(
                    app.getId(),
                    app.getJobId(),
                    app.getCompany(),
                    app.getRole(),
                    app.getStatus(),
                    app.getAppliedDate(),
                    app.getSalary(),
                    app.getNotes(),
                    app.isFollowUp(),
                    app.getStatusUpdatedAt()
            );
            return ResponseEntity.ok(Map.of("application", resp));
        } catch (IllegalArgumentException ex) {
            if ("Application not found".equals(ex.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Internal server error")
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Internal server error")
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplication(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody ApplicationDtos.CreateOrUpdateRequest request
    ) {
        try {
            applicationService.updateApplication(principal.getUserId(), id, request);
            return ResponseEntity.ok(Map.of("message", "Application updated successfully"));
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            if ("Application not found".equals(msg) || "Failed to update application".equals(msg)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", msg));
            }
            if ("All fields are required".equals(msg) || "Invalid status".equals(msg)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", msg));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Internal server error")
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Internal server error")
            );
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateApplicationStatus(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody ApplicationDtos.StatusUpdateRequest request
    ) {
        try {
            applicationService.updateApplicationStatus(principal.getUserId(), id, request.getStatus());
            return ResponseEntity.ok(Map.of("message", "Application status updated successfully"));
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            if ("Application not found".equals(msg) || "Failed to update status".equals(msg)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", msg));
            }
            if ("Invalid status".equals(msg)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", msg));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Internal server error")
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Internal server error")
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        try {
            applicationService.deleteApplication(principal.getUserId(), id);
            return ResponseEntity.ok(Map.of("message", "Application deleted successfully"));
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            if ("Application not found".equals(msg) || "Failed to delete application".equals(msg)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", msg));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Internal server error")
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Internal server error")
            );
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles(@AuthenticationPrincipal UserPrincipal principal) {
        try {
            List<String> roles = applicationService.getUniqueRoles(principal.getUserId());
            return ResponseEntity.ok(Map.of("roles", roles));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Internal server error")
            );
        }
    }
}

