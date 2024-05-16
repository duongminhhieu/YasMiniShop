package com.learning.yasminishop.auth.dto.request;

import com.learning.yasminishop.common.validator.FieldNotEmpty.FieldNotEmptyConstraint;
import com.learning.yasminishop.common.validator.FieldNotNull.FieldNotNullConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @FieldNotNullConstraint(field = "email", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "email", message = "FIELD_NOT_EMPTY")
    @Email(message = "INVALID_EMAIL", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @FieldNotNullConstraint(field = "password", message = "FIELD_NOT_NULL")
    @Size(min = 6, message = "INVALID_PASSWORD")
    private String password;

}
