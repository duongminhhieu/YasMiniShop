package com.learning.yasminishop.controller;

import com.learning.yasminishop.statistic.StatisticService;
import com.learning.yasminishop.statistic.dto.response.CustomerStatisticResponse;
import com.learning.yasminishop.statistic.dto.response.OrderStatisticResponse;
import com.learning.yasminishop.statistic.dto.response.StatisticResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class StatisticControllerTest {

    @MockBean
    private StatisticService statisticService;

    @Autowired
    private MockMvc mockMvc;

    private StatisticResponse statisticResponse;

    @BeforeEach
    void setUp() {
        CustomerStatisticResponse customerStatistic = CustomerStatisticResponse.builder()
                .totalCustomers(12L)
                .totalActiveCustomers(10L)
                .totalInactiveCustomers(2L)
                .build();

        OrderStatisticResponse orderStatistic = OrderStatisticResponse.builder()
                .totalOrders(29L)
                .pendingOrders(3L)
                .deliveringOrders(5L)
                .completedOrders(20L)
                .canceledOrders(1L)
                .build();

        statisticResponse = StatisticResponse.builder()
                .totalCategories(5L)
                .totalProducts(50L)
                .revenue(BigDecimal.valueOf(2_000_000))
                .customerStatistic(customerStatistic)
                .orderStatistic(orderStatistic)
                .topProducts(List.of())
                .latestOrders(List.of())
                .build();
    }

    @Nested
    class HappyCase {

        @Test
        @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
        void getStatistics_adminUser_success() throws Exception {
            // GIVEN
            when(statisticService.getStatistics()).thenReturn(statisticResponse);

            // WHEN THEN
            mockMvc.perform(MockMvcRequestBuilders.get("/statistics"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("internalCode").value(1000))
                    .andExpect(jsonPath("result.totalCategories").value(5))
                    .andExpect(jsonPath("result.totalProducts").value(50))
                    .andExpect(jsonPath("result.revenue").value(2_000_000))
                    .andExpect(jsonPath("result.customerStatistic.totalCustomers").value(12))
                    .andExpect(jsonPath("result.customerStatistic.totalActiveCustomers").value(10))
                    .andExpect(jsonPath("result.customerStatistic.totalInactiveCustomers").value(2))
                    .andExpect(jsonPath("result.orderStatistic.totalOrders").value(29))
                    .andExpect(jsonPath("result.orderStatistic.pendingOrders").value(3))
                    .andExpect(jsonPath("result.orderStatistic.completedOrders").value(20));
        }

    }

    @Nested
    class UnHappyCase {

        @Test
        void getStatistics_unauthenticated_failure() throws Exception {
            // WHEN THEN
            mockMvc.perform(MockMvcRequestBuilders.get("/statistics"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("internalCode").value(1005));
        }

        @Test
        @WithMockUser(username = "user@test.com", roles = {"USER"})
        void getStatistics_nonAdminUser_forbidden() throws Exception {
            // WHEN THEN
            mockMvc.perform(MockMvcRequestBuilders.get("/statistics"))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("internalCode").value(1004));
        }

    }

}
