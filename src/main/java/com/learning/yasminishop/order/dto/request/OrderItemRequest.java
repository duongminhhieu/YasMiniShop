package com.learning.yasminishop.order.dto.request;

import com.learning.yasminishop.common.validator.FieldNotEmpty.FieldNotEmptyConstraint;
import com.learning.yasminishop.common.validator.FieldNotNull.FieldNotNullConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemRequest {
    @FieldNotNullConstraint(field = "productId", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "productId", message = "FIELD_NOT_EMPTY")
    String productId;

    @FieldNotNullConstraint(field = "quantity", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "quantity", message = "FIELD_NOT_EMPTY")
    Integer quantity;
}
