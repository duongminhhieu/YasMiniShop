package com.learning.yasminishop.role.dto.request;

import com.learning.yasminishop.common.validator.FieldNotEmpty.FieldNotEmptyConstraint;
import com.learning.yasminishop.common.validator.FieldNotNull.FieldNotNullConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {

    @FieldNotNullConstraint(field = "name", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "name", message = "FIELD_NOT_EMPTY")
    String name;

    @FieldNotNullConstraint(field = "description", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "description", message = "FIELD_NOT_EMPTY")
    String description;

    @FieldNotEmptyConstraint(field = "permissions", message = "FIELD_NOT_EMPTY")
    Set<String> permissions;
}
