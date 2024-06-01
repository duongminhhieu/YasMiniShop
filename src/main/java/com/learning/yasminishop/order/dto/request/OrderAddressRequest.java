package com.learning.yasminishop.order.dto.request;

import com.learning.yasminishop.common.validator.FieldNotEmpty.FieldNotEmptyConstraint;
import com.learning.yasminishop.common.validator.FieldNotNull.FieldNotNullConstraint;
import jakarta.validation.constraints.Max;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderAddressRequest {

    @FieldNotNullConstraint(field = "contactName", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "contactName", message = "FIELD_NOT_EMPTY")
    String contactName;

    @FieldNotNullConstraint(field = "phone", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "phone", message = "FIELD_NOT_EMPTY")
    @Max(value = 10, message = "INVALID_PHONE")
    String phone;

    @FieldNotNullConstraint(field = "addressLine1", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "addressLine1", message = "FIELD_NOT_EMPTY")
    String addressLine1;

    @FieldNotNullConstraint(field = "addressLine2", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "addressLine2", message = "FIELD_NOT_EMPTY")
    String addressLine2;
}
