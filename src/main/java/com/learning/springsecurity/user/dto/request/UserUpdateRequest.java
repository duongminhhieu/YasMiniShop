package com.learning.springsecurity.user.dto.request;

import com.learning.springsecurity.common.validator.FieldNotEmpty.FieldNotEmptyConstraint;
import com.learning.springsecurity.common.validator.FieldNotNull.FieldNotNullConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    @FieldNotNullConstraint(field = "firstName", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "firstName", message = "FIELD_NOT_EMPTY")
    String firstName;

    @FieldNotNullConstraint(field = "lastName", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "lastName", message = "FIELD_NOT_EMPTY")
    String lastName;

    @FieldNotNullConstraint(field = "password", message = "FIELD_NOT_NULL")
    String password;

    @FieldNotNullConstraint(field = "roles", message = "FIELD_NOT_NULL")
    Set<String> roles;
}
