package com.learning.yasminishop.product.dto.request;

import com.learning.yasminishop.common.validator.FieldNotEmpty.FieldNotEmptyConstraint;
import com.learning.yasminishop.common.validator.FieldNotNull.FieldNotNullConstraint;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
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

    @FieldNotNullConstraint(field = "sku", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "sku", message = "FIELD_NOT_EMPTY")
    String sku;

    @FieldNotEmptyConstraint(field = "isFeatured", message = "FIELD_NOT_EMPTY")
    Boolean isFeatured;

    @FieldNotNullConstraint(field = "quantity", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "quantity", message = "FIELD_NOT_EMPTY")
    @Min(value = 1, message = "INVALID_QUANTITY")
    Long quantity;

    @FieldNotNullConstraint(field = "isAvailable", message = "FIELD_NOT_NULL")
    Boolean isAvailable;

    @FieldNotNullConstraint(field = "attributes", message = "FIELD_NOT_NULL")
    List<ProductAttributeRequest> attributes;

    @FieldNotNullConstraint(field = "categoryIds", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "categoryIds", message = "FIELD_NOT_EMPTY")
    Set<String> categoryIds;

    @FieldNotNullConstraint(field = "imageIds", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "imageIds", message = "FIELD_NOT_EMPTY")
    Set<String> imageIds;
}
