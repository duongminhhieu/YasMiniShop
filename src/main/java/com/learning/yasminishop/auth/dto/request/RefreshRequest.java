package com.learning.yasminishop.auth.dto.request;

import com.learning.yasminishop.common.validator.FieldNotEmpty.FieldNotEmptyConstraint;
import com.learning.yasminishop.common.validator.FieldNotNull.FieldNotNullConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshRequest {
    @FieldNotNullConstraint(field = "token", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "token", message = "FIELD_NOT_EMPTY")
    private String refreshToken;
}
