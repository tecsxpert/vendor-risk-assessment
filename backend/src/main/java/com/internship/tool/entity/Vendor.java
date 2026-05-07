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

@Entity
@Table(name = "vendors")
@Schema(description = "Vendor entity")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the vendor", example = "1")
    private Long id;

    @NotBlank
    @Column(nullable = false)
    @Schema(description = "Name of the vendor", example = "ABC Corp")
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    @Schema(description = "Email address of the vendor", example = "contact@abc.com")
    private String email;

    // ✅ Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
