package com.learning.yasminishop.service;

import com.learning.yasminishop.category.CategoryRepository;
import com.learning.yasminishop.common.entity.Order;
import com.learning.yasminishop.common.entity.Product;
import com.learning.yasminishop.common.enumeration.EOrderStatus;
import com.learning.yasminishop.order.OrderRepository;
import com.learning.yasminishop.product.ProductRepository;
import com.learning.yasminishop.statistic.StatisticService;
import com.learning.yasminishop.statistic.dto.response.StatisticResponse;
import com.learning.yasminishop.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
@TestPropertySource("/test.properties")
class StatisticServiceTest {

    @Autowired
    private StatisticService statisticService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private UserRepository userRepository;

    private Product product;
    private Order order;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id("product-1")
                .name("Product 1")
                .description("Product 1 description")
                .price(BigDecimal.valueOf(100_000))
                .quantity(10L)
                .slug("product-1")
                .sku("sku-1")
                .isFeatured(true)
                .isAvailable(true)
                .build();

        order = Order.builder()
                .id("order-1")
                .totalPrice(BigDecimal.valueOf(100_000))
                .status(EOrderStatus.COMPLETED)
                .build();
    }

    @Nested
    class HappyCase {

        @Test
        void getStatistics_validRequest_success() {
            // GIVEN
            when(userRepository.countByRoles_NameAndIsActive(anyString(), anyBoolean()))
                    .thenReturn(10L)
                    .thenReturn(2L);
            when(orderRepository.countByStatus(EOrderStatus.PENDING)).thenReturn(3L);
            when(orderRepository.countByStatus(EOrderStatus.DELIVERING)).thenReturn(5L);
            when(orderRepository.countByStatus(EOrderStatus.COMPLETED)).thenReturn(20L);
            when(orderRepository.countByStatus(EOrderStatus.CANCELED)).thenReturn(1L);
            when(categoryRepository.count()).thenReturn(5L);
            when(productRepository.count()).thenReturn(50L);
            when(orderRepository.findTop10ByOrderByCreatedDateDesc()).thenReturn(List.of(order));
            when(productRepository.findTop10ByOrderByQuantityDesc()).thenReturn(List.of(product));
            when(orderRepository.sumTotalAmountByStatus(any(EOrderStatus.class)))
                    .thenReturn(BigDecimal.valueOf(2_000_000));

            // WHEN
            StatisticResponse result = statisticService.getStatistics();

            // THEN
            assertThat(result).isNotNull();
            assertThat(result.getTotalCategories()).isEqualTo(5L);
            assertThat(result.getTotalProducts()).isEqualTo(50L);
            assertThat(result.getRevenue()).isEqualByComparingTo(BigDecimal.valueOf(2_000_000));
            assertThat(result.getCustomerStatistic()).isNotNull();
            assertThat(result.getCustomerStatistic().getTotalActiveCustomers()).isEqualTo(10L);
            assertThat(result.getCustomerStatistic().getTotalInactiveCustomers()).isEqualTo(2L);
            assertThat(result.getCustomerStatistic().getTotalCustomers()).isEqualTo(12L);
            assertThat(result.getOrderStatistic()).isNotNull();
            assertThat(result.getOrderStatistic().getPendingOrders()).isEqualTo(3L);
            assertThat(result.getOrderStatistic().getDeliveringOrders()).isEqualTo(5L);
            assertThat(result.getOrderStatistic().getCompletedOrders()).isEqualTo(20L);
            assertThat(result.getOrderStatistic().getCanceledOrders()).isEqualTo(1L);
            assertThat(result.getOrderStatistic().getTotalOrders()).isEqualTo(29L);
        }

        @Test
        void getStatistics_emptyData_success() {
            // GIVEN
            when(userRepository.countByRoles_NameAndIsActive(anyString(), anyBoolean()))
                    .thenReturn(0L)
                    .thenReturn(0L);
            when(orderRepository.countByStatus(any(EOrderStatus.class))).thenReturn(0L);
            when(categoryRepository.count()).thenReturn(0L);
            when(productRepository.count()).thenReturn(0L);
            when(orderRepository.findTop10ByOrderByCreatedDateDesc()).thenReturn(List.of());
            when(productRepository.findTop10ByOrderByQuantityDesc()).thenReturn(List.of());
            when(orderRepository.sumTotalAmountByStatus(any(EOrderStatus.class))).thenReturn(null);

            // WHEN
            StatisticResponse result = statisticService.getStatistics();

            // THEN
            assertThat(result).isNotNull();
            assertThat(result.getTotalCategories()).isEqualTo(0L);
            assertThat(result.getTotalProducts()).isEqualTo(0L);
            assertThat(result.getRevenue()).isNull();
            assertThat(result.getCustomerStatistic().getTotalCustomers()).isEqualTo(0L);
            assertThat(result.getOrderStatistic().getTotalOrders()).isEqualTo(0L);
        }

    }

}
