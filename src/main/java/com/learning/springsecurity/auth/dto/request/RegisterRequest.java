package com.learning.springsecurity.auth.dto.request;

import com.learning.springsecurity.auth.validator.FieldNotEmpty.FieldNotEmptyConstraint;
import com.learning.springsecurity.auth.validator.FieldNotNull.FieldNotNullConstraint;
import com.learning.springsecurity.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @FieldNotNullConstraint(field = "firstName", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "firstName", message = "FIELD_NOT_EMPTY")
    private String firstName;

    @FieldNotNullConstraint(field = "lastName", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "lastName", message = "FIELD_NOT_EMPTY")
    private String lastName;

    @FieldNotNullConstraint(field = "email", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "email", message = "FIELD_NOT_EMPTY")
    @Email(message = "INVALID_EMAIL", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @FieldNotNullConstraint(field = "age", message = "FIELD_NOT_NULL")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Integer age;

    @FieldNotNullConstraint(field = "password", message = "FIELD_NOT_NULL")
    @Size(min = 6, message = "INVALID_PASSWORD")
    private String password;

    @FieldNotNullConstraint(field = "role", message = "FIELD_NOT_NULL")
    private Role role;
}
