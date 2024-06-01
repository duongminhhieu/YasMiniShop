package com.learning.yasminishop.statistic;

import com.learning.yasminishop.category.CategoryRepository;
import com.learning.yasminishop.common.constant.PredefinedRole;
import com.learning.yasminishop.common.entity.Order;
import com.learning.yasminishop.common.entity.Product;
import com.learning.yasminishop.common.enumeration.EOrderStatus;
import com.learning.yasminishop.order.OrderRepository;
import com.learning.yasminishop.product.ProductRepository;
import com.learning.yasminishop.statistic.dto.response.CustomerStatisticResponse;
import com.learning.yasminishop.statistic.dto.response.OrderStatisticResponse;
import com.learning.yasminishop.statistic.dto.response.StatisticResponse;
import com.learning.yasminishop.statistic.mapper.StatisticMapper;
import com.learning.yasminishop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StatisticService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final StatisticMapper statisticMapper;


    @PreAuthorize("hasRole('ADMIN')")
    public StatisticResponse getStatistics() {

        Long totalActiveCustomers = userRepository.countByRoles_NameAndIsActive(PredefinedRole.USER_ROLE, true);
        Long totalInactiveCustomers = userRepository.countByRoles_NameAndIsActive(PredefinedRole.USER_ROLE, false);
        CustomerStatisticResponse customerStatistic = statisticMapper.toCustomerStatisticResponse( totalActiveCustomers, totalInactiveCustomers);

        Long pendingOrders = orderRepository.countByStatus(EOrderStatus.PENDING);
        Long deliveringOrders = orderRepository.countByStatus(EOrderStatus.DELIVERING);
        Long completedOrders = orderRepository.countByStatus(EOrderStatus.COMPLETED);
        Long canceledOrders = orderRepository.countByStatus(EOrderStatus.CANCELED);

        OrderStatisticResponse orderStatistic = statisticMapper.toOrderStatisticResponse(pendingOrders, deliveringOrders, completedOrders, canceledOrders);

        Long totalCategories = categoryRepository.count();
        Long totalProducts = productRepository.count();
        List<Order> latestOrders = orderRepository.findTop10ByOrderByCreatedDateDesc();
        List<Product> topProducts = productRepository.findTop10ByOrderByQuantityDesc();
        BigDecimal revenue = orderRepository.sumTotalAmountByStatus(EOrderStatus.COMPLETED);


        return statisticMapper.toStatisticResponse(totalCategories, totalProducts, revenue, customerStatistic, orderStatistic, topProducts, latestOrders);
    }

}
