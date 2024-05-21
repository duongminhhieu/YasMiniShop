package com.learning.yasminishop.category.dto.request;

import com.learning.yasminishop.common.validator.FieldNotNull.FieldNotNullConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryIds {

    @FieldNotNullConstraint(field = "ids", message = "FIELD_NOT_NULL")
    List<String> ids;
}
