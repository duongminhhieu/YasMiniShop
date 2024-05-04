package com.learning.springsecurity.auth.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequest {
    @NotEmpty(message = "Token is required")
    private String token;
}
