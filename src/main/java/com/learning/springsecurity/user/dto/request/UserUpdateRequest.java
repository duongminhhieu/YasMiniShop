package com.learning.springsecurity.user.dto.request;

import com.learning.springsecurity.common.validator.FieldNotEmpty.FieldNotEmptyConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    @FieldNotEmptyConstraint(field = "firstName", message = "FIELD_NOT_EMPTY")
    String firstName;

    @FieldNotEmptyConstraint(field = "lastName", message = "FIELD_NOT_EMPTY")
    String lastName;

    @FieldNotEmptyConstraint(field = "password", message = "FIELD_NOT_EMPTY")
    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;

    @FieldNotEmptyConstraint(field = "roles", message = "FIELD_NOT_EMPTY")
    Set<String> roles;
}
