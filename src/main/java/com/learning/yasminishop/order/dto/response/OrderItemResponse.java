package com.learning.yasminishop.order.dto.response;

import com.learning.yasminishop.product.dto.response.ProductResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {

    String id;

    Integer quantity;

    BigDecimal price;

    ProductResponse product;

}
