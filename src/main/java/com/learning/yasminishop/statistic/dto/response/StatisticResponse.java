package com.learning.yasminishop.statistic.dto.response;

import com.learning.yasminishop.order.dto.response.OrderAdminResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticResponse {

    Long totalCategories;

    Long totalProducts;

    BigDecimal revenue;

    CustomerStatisticResponse customerStatistic;

    OrderStatisticResponse orderStatistic;

    List<ProductStatisticResponse> topProducts;

    List<OrderAdminResponse> latestOrders;
}
