package com.learning.springsecurity.auth.dto.request;

import com.learning.springsecurity.auth.validator.FieldNotEmpty.FieldNotEmptyConstraint;
import com.learning.springsecurity.auth.validator.FieldNotNull.FieldNotNullConstraint;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequest {
    @FieldNotNullConstraint(field = "token", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "token", message = "FIELD_NOT_EMPTY")
    private String token;
}
