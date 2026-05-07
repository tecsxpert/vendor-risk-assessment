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


@RestController
@RequestMapping("/auth")
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