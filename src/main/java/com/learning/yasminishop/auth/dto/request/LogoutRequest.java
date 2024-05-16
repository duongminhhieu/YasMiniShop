package com.learning.yasminishop.auth.dto.request;

import com.learning.yasminishop.common.validator.FieldNotEmpty.FieldNotEmptyConstraint;
import com.learning.yasminishop.common.validator.FieldNotNull.FieldNotNullConstraint;
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
