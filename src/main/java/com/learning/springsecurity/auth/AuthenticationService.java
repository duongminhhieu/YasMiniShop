package com.learning.springsecurity.auth;

import com.learning.springsecurity.auth.dto.request.AuthenticationRequest;
import com.learning.springsecurity.auth.dto.request.LogoutRequest;
import com.learning.springsecurity.auth.dto.request.RefreshRequest;
import com.learning.springsecurity.auth.dto.request.RegisterRequest;
import com.learning.springsecurity.auth.dto.response.AuthenticationResponse;
import com.learning.springsecurity.auth.exception.EmailAlreadyExistsException;
import com.learning.springsecurity.auth.exception.EmailNotFoundException;
import com.learning.springsecurity.auth.exception.InvalidTokenException;
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

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException(String.format("Email %s is already taken", registerRequest.getEmail()));
        }

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
                .orElseThrow(() -> new EmailNotFoundException(String.format("Email %s is not found", request.getEmail())));

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
                    .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

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
            throw new InvalidTokenException("Invalid refresh token");
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
            throw new InvalidTokenException("Invalid access token");
        }


    }
}
