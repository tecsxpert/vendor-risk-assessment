package com.internship.tool.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User registration request payload")
public class RegisterRequest {

    @Schema(description = "User full name", example = "John Doe")
    private String name;

    @Schema(description = "User email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "User password", example = "securePassword123")
    private String password;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}