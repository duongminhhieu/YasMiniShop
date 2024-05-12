package com.learning.springsecurity.auth;

import com.learning.springsecurity.auth.dto.request.AuthenticationRequest;
import com.learning.springsecurity.auth.dto.request.LogoutRequest;
import com.learning.springsecurity.auth.dto.request.RefreshRequest;
import com.learning.springsecurity.auth.dto.request.RegisterRequest;
import com.learning.springsecurity.auth.dto.response.APIResponse;
import com.learning.springsecurity.auth.dto.response.AuthenticationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public APIResponse<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
        return APIResponse.<AuthenticationResponse>builder()
                .result(authenticationService.register(registerRequest))
                .build();
    }

    @PostMapping("/authenticate")
    public APIResponse<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest authenticationRequest
    ) {
        return APIResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(authenticationRequest))
                .build();
    }

    @PostMapping("/refresh")
    public APIResponse<AuthenticationResponse> refresh(
            @Valid @RequestBody RefreshRequest request
    ) {
        return APIResponse.<AuthenticationResponse>builder()
                .result(authenticationService.refresh(request))
                .build();
    }

    @PostMapping("/logout")
    public APIResponse<String> logout(
            @Valid @RequestBody LogoutRequest request
    ) {
        authenticationService.logout(request);
        return APIResponse.<String>builder()
                .result("Logout successful")
                .build();
    }

}
