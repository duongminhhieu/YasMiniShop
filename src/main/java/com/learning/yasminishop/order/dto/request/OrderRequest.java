package com.learning.yasminishop.order.dto.request;

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
public class OrderRequest {

    @FieldNotNullConstraint(field = "cartItemIds", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "cartItemIds", message = "FIELD_NOT_EMPTY")
    Set<String> cartItemIds;

    @FieldNotNullConstraint(field = "orderAddress", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "orderAddress", message = "FIELD_NOT_EMPTY")
    OrderAddressRequest orderAddress;

}
