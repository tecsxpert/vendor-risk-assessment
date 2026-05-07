package com.internship.tool.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendors")
@Schema(description = "Vendor entity")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the vendor", example = "1")
    private Long id;

    @NotBlank
    @Column(name = "vendor_name", nullable = false)
    @Schema(description = "Name of the vendor", example = "ABC Corp")
    private String name;

    @Column(name = "contact_person")
    @Schema(description = "Contact person name", example = "John Smith")
    private String contactPerson;

    @Email
    @Column(nullable = false, unique = true)
    @Schema(description = "Email address of the vendor", example = "contact@abc.com")
    private String email;

    @Column(name = "phone")
    @Schema(description = "Vendor phone number", example = "+1-555-0100")
    private String phone;

    @Column(name = "risk_score")
    @Schema(description = "Risk score between 0-100", example = "45")
    private Integer riskScore = 0;

    @Column(name = "status")
    @Schema(description = "Vendor status: LOW, MEDIUM, HIGH, PENDING", example = "MEDIUM")
    private String status = "PENDING";

    @Column(name = "description", columnDefinition = "TEXT")
    @Schema(description = "Notes or AI summary", example = "Good vendor with high compliance")
    private String description;

    @Column(name = "review_date")
    @Schema(description = "Date for next review", example = "2024-06-01")
    private LocalDate reviewDate;

    @Column(name = "deleted")
    @Schema(description = "Soft delete flag", example = "false")
    private Boolean deleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ✅ Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Integer getRiskScore() { return riskScore; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDate reviewDate) { this.reviewDate = reviewDate; }

    public Boolean getDeleted() { return deleted; }
    public void setDeleted(Boolean deleted) { this.deleted = deleted; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
