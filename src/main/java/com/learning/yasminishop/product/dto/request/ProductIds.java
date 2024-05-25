package com.learning.yasminishop.product.dto.request;

import com.learning.yasminishop.common.validator.FieldNotNull.FieldNotNullConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductIds {
    @FieldNotNullConstraint(field = "ids", message = "FIELD_NOT_NULL")
    List<String> ids;
}


