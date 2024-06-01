package com.learning.yasminishop.cart.dto.request;

import com.learning.yasminishop.common.validator.FieldNotNull.FieldNotNullConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemIds {
    @FieldNotNullConstraint(field = "ids", message = "FIELD_NOT_NULL")
    List<String> ids;
}
