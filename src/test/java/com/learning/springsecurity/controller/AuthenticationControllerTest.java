package com.learning.springsecurity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.springsecurity.auth.AuthenticationService;
import com.learning.springsecurity.auth.dto.request.AuthenticationRequest;
import com.learning.springsecurity.auth.dto.request.LogoutRequest;
import com.learning.springsecurity.auth.dto.request.RefreshRequest;
import com.learning.springsecurity.auth.dto.request.RegisterRequest;
import com.learning.springsecurity.auth.dto.response.AuthenticationResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    private AuthenticationResponse authenticationResponse;
    private RegisterRequest registerRequest;
    private AuthenticationRequest authenticationRequest;
    private RefreshRequest refreshRequest;
    private LogoutRequest logoutRequest;

    @BeforeEach
    void setUp() {

        authenticationRequest = AuthenticationRequest.builder()
                .email("duongminhhieu@gmail.com")
                .password("123456")
                .build();

        registerRequest = RegisterRequest.builder()
                .firstName("Hieu")
                .lastName("Duong")
                .email("duongminhhieu@gmail.com")
                .password("123456")
                .build();

        authenticationResponse = AuthenticationResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        refreshRequest = RefreshRequest.builder()
                .refreshToken("refresh")
                .build();

        logoutRequest = LogoutRequest.builder()
                .token("token")
                .build();

    }

    @Test
    void register_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String registerRequestJson = objectMapper.writeValueAsString(registerRequest);

        when(authenticationService.register(any(RegisterRequest.class)))
                .thenReturn(authenticationResponse);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result.access_token").value("accessToken"))
                .andExpect(jsonPath("result.refresh_token").value("refreshToken"));

    }

    @Test
    void register_invalidEmail_fail() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        registerRequest.setEmail("invalidEmail");
        String registerRequestJson = objectMapper.writeValueAsString(registerRequest);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("internalCode").value(2002))
                .andExpect(jsonPath("message").value("Invalid email"));
    }

    @Test
    void authenticate_validRequest_success() throws Exception {

        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String authenticationRequestJson = objectMapper.writeValueAsString(authenticationRequest);

        when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                .thenReturn(authenticationResponse);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authenticationRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result.access_token").value("accessToken"))
                .andExpect(jsonPath("result.refresh_token").value("refreshToken"));

    }

    @Test
    void authenticate_invalidEmail_fail() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        authenticationRequest.setEmail("invalidEmail");
        String authenticationRequestJson = objectMapper.writeValueAsString(authenticationRequest);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authenticationRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("internalCode").value(2002))
                .andExpect(jsonPath("message").value("Invalid email"));
    }

    @Test
    void authenticate_invalidPassword_fail() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        authenticationRequest.setPassword("123");
        String authenticationRequestJson = objectMapper.writeValueAsString(authenticationRequest);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authenticationRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("internalCode").value(2001))
                .andExpect(jsonPath("message").value("Password must be at least 6 characters"));
    }

    @Test
    void authenticate_nullPassword_fail() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        authenticationRequest.setPassword(null);
        String authenticationRequestJson = objectMapper.writeValueAsString(authenticationRequest);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authenticationRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("internalCode").value(2004))
                .andExpect(jsonPath("message").value("\"password\" must not be null"));
    }

    @Test
    void authenticate_nullEmail_fail() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        authenticationRequest.setEmail(null);
        String authenticationRequestJson = objectMapper.writeValueAsString(authenticationRequest);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authenticationRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("internalCode").value(2004))
                .andExpect(jsonPath("message").value("\"email\" must not be null"));
    }

    @Test
    void refresh_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String refreshRequestJson = objectMapper.writeValueAsString(refreshRequest);

        when(authenticationService.refresh(any(RefreshRequest.class)))
                .thenReturn(authenticationResponse);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result.access_token").value("accessToken"))
                .andExpect(jsonPath("result.refresh_token").value("refreshToken"));

    }

    @Test
    void refresh_nullToken_fail() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        refreshRequest.setRefreshToken(null);
        String refreshRequestJson = objectMapper.writeValueAsString(refreshRequest);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("internalCode").value(2004))
                .andExpect(jsonPath("message").value("\"token\" must not be null"));
    }

    @Test
    void logout_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String logoutRequestJson = objectMapper.writeValueAsString(logoutRequest);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(logoutRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result").value("Logout successful"));
    }

    @Test
    void logout_nullToken_fail() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        logoutRequest.setToken(null);
        String logoutRequestJson = objectMapper.writeValueAsString(logoutRequest);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(logoutRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("internalCode").value(2004))
                .andExpect(jsonPath("message").value("\"token\" must not be null"));
    }

}
