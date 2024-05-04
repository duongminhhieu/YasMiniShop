package com.learning.springsecurity.auth.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshRequest {
    @NotEmpty(message = "Refresh token is required")
    private String refreshToken;
}
