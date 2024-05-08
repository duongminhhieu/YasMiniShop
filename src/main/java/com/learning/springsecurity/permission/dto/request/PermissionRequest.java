package com.learning.springsecurity.permission.dto.request;

import com.learning.springsecurity.common.validator.FieldNotEmpty.FieldNotEmptyConstraint;
import com.learning.springsecurity.common.validator.FieldNotNull.FieldNotNullConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionRequest {

    @FieldNotNullConstraint(field = "name", message = "FIELD_NOT_NULL")
    private String name;
    @FieldNotEmptyConstraint(field = "description", message = "FIELD_NOT_EMPTY")
    private String description;
}