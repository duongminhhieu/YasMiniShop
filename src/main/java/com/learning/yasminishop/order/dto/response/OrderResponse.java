package com.learning.yasminishop.order.dto.response;

import com.learning.yasminishop.common.enumeration.EOrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {

    String id;

    Integer totalQuantity;

    BigDecimal totalPrice;

    EOrderStatus status;

    OrderAddressResponse orderAddress;

    List<OrderItemResponse> orderItems;

    String createdBy;
    String createdDate;
    String lastModifiedBy;
    String lastModifiedDate;
}
