package com.learning.yasminishop.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.learning.yasminishop.user.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {

    private UserResponse user;

    private TokenResponse tokens;
}
