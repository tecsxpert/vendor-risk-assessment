package com.internship.tool.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        // Initialize JwtUtil with your secret key and expiration time
        jwtUtil = new JwtUtil("mySecretKey", 3600000); // 1 hour expiry
    }

    @Test
    void testGenerateToken() {
        String username = "testUser";

        String token = jwtUtil.generateToken(username);

        assertNotNull(token, "Token should not be null");
        assertTrue(jwtUtil.validateToken(token, username), "Token should be valid for the user");
    }

    @Test
    void testExtractUsername() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        String extracted = jwtUtil.extractUsername(token);

        assertEquals(username, extracted, "Extracted username should match original");
    }

    @Test
    void testTokenExpiration() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        assertFalse(jwtUtil.isTokenExpired(token), "Newly generated token should not be expired");
    }
}
