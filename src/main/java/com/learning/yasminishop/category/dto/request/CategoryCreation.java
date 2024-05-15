package com.learning.yasminishop.category.dto.request;

import com.learning.yasminishop.common.validator.FieldNotEmpty.FieldNotEmptyConstraint;
import com.learning.yasminishop.common.validator.FieldNotNull.FieldNotNullConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCreation {

    @FieldNotNullConstraint(field = "name", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "name", message = "FIELD_NOT_EMPTY")
    String name;

    @FieldNotNullConstraint(field = "slug", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "slug", message = "FIELD_NOT_EMPTY")
    String slug;

    @FieldNotNullConstraint(field = "description", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "description", message = "FIELD_NOT_EMPTY")
    String description;
}
