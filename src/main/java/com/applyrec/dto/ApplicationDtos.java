package com.applyrec.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class ApplicationDtos {

    public static class CreateOrUpdateRequest {
        @NotBlank
        private String company;

        @NotBlank
        private String role;

        @NotBlank
        private String status;

        @NotNull
        private LocalDate appliedDate;

        private String notes;
        private Boolean followUp;
        private String jobId;
        private BigDecimal salary;

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDate getAppliedDate() {
            return appliedDate;
        }

        public void setAppliedDate(LocalDate appliedDate) {
            this.appliedDate = appliedDate;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public Boolean getFollowUp() {
            return followUp;
        }

        public void setFollowUp(Boolean followUp) {
            this.followUp = followUp;
        }

        public String getJobId() {
            return jobId;
        }

        public void setJobId(String jobId) {
            this.jobId = jobId;
        }

        public BigDecimal getSalary() {
            return salary;
        }

        public void setSalary(BigDecimal salary) {
            this.salary = salary;
        }
    }

    public static class StatusUpdateRequest {
        @NotBlank
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class ApplicationResponse {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("job_id")
        private String jobId;

        @JsonProperty("company")
        private String company;

        @JsonProperty("role")
        private String role;

        @JsonProperty("status")
        private String status;

        @JsonProperty("applied_date")
        private LocalDate appliedDate;

        @JsonProperty("salary")
        private BigDecimal salary;

        @JsonProperty("notes")
        private String notes;

        @JsonProperty("follow_up")
        private boolean followUp;

        @JsonProperty("status_updated_at")
        private Instant statusUpdatedAt;

        public ApplicationResponse(Long id, String jobId, String company, String role, String status,
                                   LocalDate appliedDate, BigDecimal salary, String notes,
                                   boolean followUp, Instant statusUpdatedAt) {
            this.id = id;
            this.jobId = jobId;
            this.company = company;
            this.role = role;
            this.status = status;
            this.appliedDate = appliedDate;
            this.salary = salary;
            this.notes = notes;
            this.followUp = followUp;
            this.statusUpdatedAt = statusUpdatedAt;
        }

        public Long getId() {
            return id;
        }

        public String getJobId() {
            return jobId;
        }

        public String getCompany() {
            return company;
        }

        public String getRole() {
            return role;
        }

        public String getStatus() {
            return status;
        }

        public LocalDate getAppliedDate() {
            return appliedDate;
        }

        public BigDecimal getSalary() {
            return salary;
        }

        public String getNotes() {
            return notes;
        }

        public boolean isFollowUp() {
            return followUp;
        }

        public Instant getStatusUpdatedAt() {
            return statusUpdatedAt;
        }
    }
}

