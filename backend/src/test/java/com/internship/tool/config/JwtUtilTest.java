package com.internship.tool.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("Refresh token returns a string")
    void testRefreshToken() {
        String oldToken = "old-token";
        String newToken = jwtUtil.refreshToken(oldToken);

        assertThat(newToken).isNotNull();
        assertThat(newToken).isEqualTo("refreshed-jwt-token");
    }
}