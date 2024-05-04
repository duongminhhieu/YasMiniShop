package com.learning.springsecurity.auth;

import com.learning.springsecurity.auth.dto.request.AuthenticationRequest;
import com.learning.springsecurity.auth.dto.request.LogoutRequest;
import com.learning.springsecurity.auth.dto.request.RefreshRequest;
import com.learning.springsecurity.auth.dto.request.RegisterRequest;
import com.learning.springsecurity.auth.dto.response.AuthenticationResponse;
import com.learning.springsecurity.configs.exception.AppException;
import com.learning.springsecurity.configs.exception.ErrorCode;
import com.learning.springsecurity.configs.security.JwtService;
import com.learning.springsecurity.token.InvalidToken;
import com.learning.springsecurity.token.InvalidTokenRepository;
import com.learning.springsecurity.user.User;
import com.learning.springsecurity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final InvalidTokenRepository invalidTokenRepository;


    public AuthenticationResponse register(RegisterRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.getEmail())) throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);

        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();

        userRepository.save(user);
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(
            AuthenticationRequest request
    ) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public AuthenticationResponse refresh(RefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        if (jwtService.isTokenValid(refreshToken)) {

            String userEmail = jwtService.extractUserEmail(refreshToken);
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

            // Add the refresh token to the invalid token list
            InvalidToken invalidToken = InvalidToken.builder()
                    .idToken(jwtService.extractIdToken(refreshToken))
                    .expiryDate(jwtService.extractExpiration(refreshToken))
                    .build();

            invalidTokenRepository.save(invalidToken);

            // Generate new access token and refresh token
            var accessToken = jwtService.generateAccessToken(user);
            var newRefreshToken = jwtService.generateRefreshToken(user);

            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        } else {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    public void logout(LogoutRequest request) {
        if (jwtService.isTokenValid(request.getToken())) {
            String idToken = jwtService.extractIdToken(request.getToken());
            invalidTokenRepository.save(InvalidToken.builder()
                    .idToken(idToken)
                    .expiryDate(jwtService.extractExpiration(request.getToken()))
                    .build());
        } else {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }


    }
}
