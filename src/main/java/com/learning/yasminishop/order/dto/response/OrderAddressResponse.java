package com.learning.yasminishop.order.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderAddressResponse {

    String id;

    String contactName;

    String phone;

    String addressLine1;

    String addressLine2;
}
