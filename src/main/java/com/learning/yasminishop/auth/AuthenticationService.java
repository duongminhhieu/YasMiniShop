package com.learning.yasminishop.auth;

import com.learning.yasminishop.auth.dto.request.AuthenticationRequest;
import com.learning.yasminishop.auth.dto.request.LogoutRequest;
import com.learning.yasminishop.auth.dto.request.RefreshRequest;
import com.learning.yasminishop.auth.dto.request.RegisterRequest;
import com.learning.yasminishop.auth.dto.response.AuthenticationResponse;
import com.learning.yasminishop.auth.dto.response.TokenResponse;
import com.learning.yasminishop.common.configs.security.JwtService;
import com.learning.yasminishop.common.constant.PredefinedRole;
import com.learning.yasminishop.common.entity.InvalidToken;
import com.learning.yasminishop.common.entity.Role;
import com.learning.yasminishop.common.entity.User;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.common.exception.ErrorCode;
import com.learning.yasminishop.role.RoleRepository;
import com.learning.yasminishop.token.InvalidTokenRepository;
import com.learning.yasminishop.user.UserRepository;
import com.learning.yasminishop.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {


    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final UserRepository userRepository;
    private final InvalidTokenRepository invalidTokenRepository;
    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    @Transactional
    public AuthenticationResponse register(RegisterRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.getEmail()))
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE)
                .ifPresent(roles::add);

        User user = userMapper.toUser(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(roles);

        userRepository.save(user);

        var tokens = TokenResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();

        return AuthenticationResponse.builder()
                .tokens(tokens)
                .build();
    }

    public AuthenticationResponse authenticate(
            AuthenticationRequest request
    ) {

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_OR_PASSWORD_INCORRECT));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.EMAIL_OR_PASSWORD_INCORRECT);
        }

        // check if user is active
        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new AppException(ErrorCode.USER_NOT_ACTIVE);
        }

        var tokens = TokenResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();

        return AuthenticationResponse.builder()
                .user(userMapper.toUserResponse(user))
                .tokens(tokens)
                .build();
    }

    @Transactional
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
            var tokens = TokenResponse.builder()
                    .accessToken(jwtService.generateAccessToken(user))
                    .refreshToken(jwtService.generateRefreshToken(user))
                    .build();

            return AuthenticationResponse.builder()
                    .tokens(tokens)
                    .build();
        } else {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    @Transactional
    public void logout(LogoutRequest request) {
        if (jwtService.isTokenValid(request.getToken())) {
            String idToken = jwtService.extractIdToken(request.getToken());
            invalidTokenRepository.save(InvalidToken.builder()
                    .idToken(idToken)
                    .expiryDate(jwtService.extractExpiration(request.getToken()))
                    .build());
            SecurityContextHolder.clearContext();
        } else {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }


    }
}
