package com.internship.tool.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.internship.tool.service.UserService;
import com.internship.tool.config.JwtUtil;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService; // Mock the service dependency

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("Login success returns 200 OK")
    void testLoginSuccess() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "user")
                .param("password", "password"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Login failure returns 401 Unauthorized")
    void testLoginFailure() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "user")
                .param("password", "wrong"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Register success returns 201 Created")
    void testRegisterSuccess() throws Exception {
        String jsonBody = "{ \"username\": \"newuser\", \"password\": \"newpass\" }";

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Register failure returns 400 Bad Request")
    void testRegisterFailure() throws Exception {
        String jsonBody = "{ \"username\": \"\", \"password\": \"\" }";

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isBadRequest());
    }
}
