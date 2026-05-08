package com.internship.tool.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // ==========================================
    // Login user and return JWT token
    // POST /auth/login
    // ==========================================
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(example = "{\"token\":\"jwt-token-here\"}"))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials",
                content = @Content)
    })
    public Map<String, String> login(@RequestBody LoginRequest request) {

        String token = userService.loginUser(
                request.getEmail(),
                request.getPassword()
        );

        return Map.of("token", token);
    }

    // ==========================================
    // Register new user
    // POST /auth/register
    // Default role = VIEWER
    // ==========================================
    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user with default VIEWER role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully",
                content = @Content(mediaType = "text/plain",
                        schema = @Schema(example = "User registered successfully"))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content)
    })
    public String register(@RequestBody RegisterRequest request) {

        userService.registerUser(request);

        return "User registered successfully";
    }

    // ==========================================
    // Refresh token
    // POST /auth/refresh
    // ==========================================
    @PostMapping("/refresh")
    public Map<String, String> refreshToken(
            @RequestHeader("Authorization") String authHeader) {

        String oldToken = authHeader.replace("Bearer ", "");
        String newToken = jwtUtil.refreshToken(oldToken);

        return Map.of("token", newToken);
    }
}