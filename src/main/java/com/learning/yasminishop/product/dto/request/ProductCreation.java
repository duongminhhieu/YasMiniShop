package com.learning.yasminishop.product.dto.request;

import com.learning.yasminishop.common.validator.FieldNotEmpty.FieldNotEmptyConstraint;
import com.learning.yasminishop.common.validator.FieldNotNull.FieldNotNullConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreation {
    @FieldNotNullConstraint(field = "name", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "name", message = "FIELD_NOT_EMPTY")
    String name;

    @FieldNotNullConstraint(field = "description", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "description", message = "FIELD_NOT_EMPTY")
    String description;

    @FieldNotNullConstraint(field = "price", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "price", message = "FIELD_NOT_EMPTY")
    BigDecimal price;

    @FieldNotNullConstraint(field = "slug", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "slug", message = "FIELD_NOT_EMPTY")
    String slug;

    @FieldNotNullConstraint(field = "quantity", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "quantity", message = "FIELD_NOT_EMPTY")
    Long quantity;

    @FieldNotNullConstraint(field = "categoryIds", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "categoryIds", message = "FIELD_NOT_EMPTY")
    Set<String> categoryIds;
}
