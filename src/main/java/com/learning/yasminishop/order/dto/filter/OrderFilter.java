package com.learning.yasminishop.order.dto.filter;

import com.learning.yasminishop.common.enumeration.EOrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderFilter {

    EOrderStatus status;

    String[] orderBy = {"createdDate"};

    String sortBy = "desc";

    Integer page = 1;

    Integer itemsPerPage = 10;

}
