package com.learning.yasminishop.statistic.mapper;

import com.learning.yasminishop.common.entity.Order;
import com.learning.yasminishop.common.entity.Product;
import com.learning.yasminishop.statistic.dto.response.CustomerStatisticResponse;
import com.learning.yasminishop.statistic.dto.response.OrderStatisticResponse;
import com.learning.yasminishop.statistic.dto.response.StatisticResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface StatisticMapper {

    @Mapping(target = "totalCustomers", expression = "java(totalActiveCustomers + totalInactiveCustomers)")
    CustomerStatisticResponse toCustomerStatisticResponse(Long totalActiveCustomers,
                                                          Long totalInactiveCustomers);

    @Mapping(target = "totalOrders", expression = "java(pendingOrders + deliveringOrders + completedOrders + canceledOrders)")
    OrderStatisticResponse toOrderStatisticResponse(Long pendingOrders,
                                                    Long deliveringOrders,
                                                    Long completedOrders ,
                                                    Long canceledOrders);

    StatisticResponse toStatisticResponse(Long totalCategories,
                                          Long totalProducts,
                                          BigDecimal revenue,
                                          CustomerStatisticResponse customerStatistic,
                                          OrderStatisticResponse orderStatistic,
                                          List<Product> topProducts,
                                          List<Order> latestOrders);

}
