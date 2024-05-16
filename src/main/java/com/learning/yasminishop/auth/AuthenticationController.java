package com.learning.yasminishop.auth;

import com.learning.yasminishop.auth.dto.request.AuthenticationRequest;
import com.learning.yasminishop.auth.dto.request.LogoutRequest;
import com.learning.yasminishop.auth.dto.request.RefreshRequest;
import com.learning.yasminishop.auth.dto.request.RegisterRequest;
import com.learning.yasminishop.common.dto.APIResponse;
import com.learning.yasminishop.auth.dto.response.AuthenticationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
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
