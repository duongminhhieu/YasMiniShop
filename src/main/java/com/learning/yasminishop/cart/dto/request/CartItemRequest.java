package com.learning.yasminishop.cart.dto.request;

import com.learning.yasminishop.common.validator.FieldNotEmpty.FieldNotEmptyConstraint;
import com.learning.yasminishop.common.validator.FieldNotNull.FieldNotNullConstraint;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemRequest {

    @FieldNotNullConstraint(field = "productId", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "productId", message = "FIELD_NOT_EMPTY")
    String productId;

    @FieldNotNullConstraint(field = "quantity", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "quantity", message = "FIELD_NOT_EMPTY")
    @Min(value = 1, message = "INVALID_QUANTITY")
    Integer quantity;

}
