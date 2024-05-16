package com.learning.yasminishop.service;

import com.learning.yasminishop.auth.AuthenticationService;
import com.learning.yasminishop.auth.dto.request.AuthenticationRequest;
import com.learning.yasminishop.auth.dto.request.LogoutRequest;
import com.learning.yasminishop.auth.dto.request.RefreshRequest;
import com.learning.yasminishop.auth.dto.request.RegisterRequest;
import com.learning.yasminishop.common.configs.security.JwtService;
import com.learning.yasminishop.common.entity.Role;
import com.learning.yasminishop.common.entity.User;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.role.RoleRepository;
import com.learning.yasminishop.token.InvalidTokenRepository;
import com.learning.yasminishop.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
@TestPropertySource("/test.properties")
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private InvalidTokenRepository invalidTokenRepository;

    private RegisterRequest registerRequest;
    private AuthenticationRequest authenticationRequest;
    private RefreshRequest refreshRequest;
    private LogoutRequest logoutRequest;

    @BeforeEach
    void setUp() {

        registerRequest = RegisterRequest.builder()
                .firstName("Hieu")
                .lastName("Duong")
                .email("duongminhhieu@gmail.com")
                .password("123456")
                .build();

        authenticationRequest = AuthenticationRequest.builder()
                .email("duongminhhieu@gmail.com")
                .password("123456")
                .build();

        refreshRequest = RefreshRequest.builder()
                .refreshToken("refresh")
                .build();

        logoutRequest = LogoutRequest.builder()
                .token("token")
                .build();
    }

    @Test
    void register_validRequest_success() {

        // GIVEN
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findById(anyString()))
                .thenReturn(Optional.of(Role.builder()
                        .name("USER")
                        .description("User role")
                        .build()));

        when(userRepository.save(any()))
                .thenReturn(new User());

        when(jwtService.generateAccessToken(any()))
                .thenReturn("accessToken");

        when(jwtService.generateRefreshToken(any()))
                .thenReturn("refreshToken");

        // WHEN
        var response = authenticationService.register(registerRequest);

        // THEN
        verify(userRepository, times(1)).save(any());
        assertThat(response.getAccessToken()).isEqualTo("accessToken");
        assertThat(response.getRefreshToken()).isEqualTo("refreshToken");
    }

    @Test
    void register_emailAlreadyExists_throwException() {

        // GIVEN
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> {
                    authenticationService.register(registerRequest);
                }
        );

        // THEN
        assertThat(exception.getErrorCode().getInternalCode()).isEqualTo(1002);
        assertThat(exception.getErrorCode().getMessage()).isEqualTo("Email already exists");
    }

    @Test
    void authenticate_validRequest_success() {
        // GIVEN

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(User.builder()
                        .id("abc-123")
                        .email("duongminhhieu@gmail.com")
                        .password("123456")
                        .build()));
        when(jwtService.generateAccessToken(any()))
                .thenReturn("accessToken");
        when(jwtService.generateRefreshToken(any()))
                .thenReturn("refreshToken");

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(true);

        // WHEN
        var response = authenticationService.authenticate(authenticationRequest);

        // THEN
        assertThat(response.getAccessToken()).isEqualTo("accessToken");
        assertThat(response.getRefreshToken()).isEqualTo("refreshToken");
    }

    @Test
    void authenticate_invalidPassword_throwException() {
        // GIVEN
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(User.builder()
                        .id("abc-123")
                        .email("duongminhhieu@gmail.com")
                        .password("123456")
                        .build()));
        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(false);

        // WHEN
        var exception = assertThrows(AppException.class, () -> {
                    authenticationService.authenticate(authenticationRequest);
                }
        );

        // THEN
        assertThat(exception.getErrorCode().getInternalCode()).isEqualTo(1005);
        assertThat(exception.getErrorCode().getMessage()).isEqualTo("Unauthenticated");
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(jwtService, never()).generateAccessToken(any());
        verify(jwtService, never()).generateRefreshToken(any());
    }

    @Test
    void authenticate_userNotFound_throwException() {
        // GIVEN
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        // WHEN
        var exception = assertThrows(AppException.class, () -> {
                    authenticationService.authenticate(authenticationRequest);
                }
        );

        // THEN
        assertThat(exception.getErrorCode().getInternalCode()).isEqualTo(1005);
        assertThat(exception.getErrorCode().getMessage()).isEqualTo("Unauthenticated");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateAccessToken(any());
        verify(jwtService, never()).generateRefreshToken(any());
    }

    @Test
    void refresh_validRequest_success() {
        // GIVEN
        when(jwtService.isTokenValid(anyString())).thenReturn(true);
        when(jwtService.extractUserEmail(anyString())).thenReturn("duongminhhieu@gmail.com");
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(User.builder()
                        .id("abc-123")
                        .email("duongminhhieu@gmail.com")
                        .password("123456")
                        .build()));
        when(jwtService.extractIdToken(anyString())).thenReturn("idToken");
        when(jwtService.extractExpiration(anyString()))
                .thenReturn(Date.from(new Date().toInstant().plusSeconds(3600)));
        when(jwtService.generateAccessToken(any()))
                .thenReturn("accessToken");
        when(jwtService.generateRefreshToken(any()))
                .thenReturn("refreshToken");

        // WHEN
        var response = authenticationService.refresh(refreshRequest);

        // THEN
        assertThat(response.getAccessToken()).isEqualTo("accessToken");
        assertThat(response.getRefreshToken()).isEqualTo("refreshToken");
        verify(invalidTokenRepository, times(1)).save(any());
    }

    @Test
    void refresh_invalidToken_throwException() {
        // GIVEN
        when(jwtService.isTokenValid(anyString())).thenReturn(false);

        // WHEN
        var exception = assertThrows(AppException.class, () -> {
                    authenticationService.refresh(refreshRequest);
                }
        );

        // THEN
        assertThat(exception.getErrorCode().getInternalCode()).isEqualTo(1001);
        assertThat(exception.getErrorCode().getMessage()).isEqualTo("Invalid token");
        verify(userRepository, never()).findByEmail(anyString());
        verify(jwtService, never()).generateAccessToken(any());
        verify(jwtService, never()).generateRefreshToken(any());
        verify(invalidTokenRepository, never()).save(any());
    }

    @Test
    void refresh_userNotFound_throwException() {
        // GIVEN
        when(jwtService.isTokenValid(anyString())).thenReturn(true);
        when(jwtService.extractUserEmail(anyString())).thenReturn("hehe@gmail.com");

        // WHEN
        var exception = assertThrows(AppException.class, () -> {
                    authenticationService.refresh(refreshRequest);
                }
        );

        // THEN
        assertThat(exception.getErrorCode().getInternalCode()).isEqualTo(1001);
        assertThat(exception.getErrorCode().getMessage()).isEqualTo("Invalid token");
    }

    @Test
    void logout_validRequest_success() {
        // GIVEN
        when(jwtService.isTokenValid(anyString())).thenReturn(true);
        when(jwtService.extractIdToken(anyString())).thenReturn("idToken");

        // WHEN
        authenticationService.logout(logoutRequest);
        // THEN
        verify(invalidTokenRepository, times(1)).save(any());
    }

    @Test
    void logout_invalidToken_throwException() {
        // GIVEN
        when(jwtService.isTokenValid(anyString())).thenReturn(false);

        // WHEN
        var exception = assertThrows(AppException.class, () -> {
                    authenticationService.logout(logoutRequest);
                }
        );

        // THEN
        assertThat(exception.getErrorCode().getInternalCode()).isEqualTo(1001);
        assertThat(exception.getErrorCode().getMessage()).isEqualTo("Invalid token");
        verify(invalidTokenRepository, never()).save(any());
    }
}
