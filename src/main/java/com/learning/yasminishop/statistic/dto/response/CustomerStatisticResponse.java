package com.learning.yasminishop.statistic.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerStatisticResponse {
    Long totalCustomers;
    Long totalActiveCustomers;
    Long totalInactiveCustomers;
}
