package com.learning.yasminishop.rating.dto.request;

import com.learning.yasminishop.common.validator.FieldNotEmpty.FieldNotEmptyConstraint;
import com.learning.yasminishop.common.validator.FieldNotNull.FieldNotNullConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingRequest {

    @FieldNotNullConstraint(field = "productId", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "productId", message = "FIELD_NOT_EMPTY")
    String productId;

    @FieldNotNullConstraint(field = "rating", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "rating", message = "FIELD_NOT_EMPTY")
    @Min(value = 1, message = "INVALID_RATING")
    @Max(value = 5, message = "INVALID_RATING")
    Integer star;

    @FieldNotNullConstraint(field = "comment", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "comment", message = "FIELD_NOT_EMPTY")
    @Size(min = 10, max = 1000, message = "INVALID_COMMENT")
    String comment;
}
