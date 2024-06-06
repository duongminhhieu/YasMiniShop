package com.learning.yasminishop.service;


import com.learning.yasminishop.cart.CartItemRepository;
import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.common.entity.*;
import com.learning.yasminishop.common.enumeration.EOrderStatus;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.notification.NotificationRepository;
import com.learning.yasminishop.order.OrderRepository;
import com.learning.yasminishop.order.OrderService;
import com.learning.yasminishop.order.dto.filter.OrderFilter;
import com.learning.yasminishop.order.dto.request.OrderAddressRequest;
import com.learning.yasminishop.order.dto.request.OrderRequest;
import com.learning.yasminishop.order.dto.response.OrderAdminResponse;
import com.learning.yasminishop.order.dto.response.OrderResponse;
import com.learning.yasminishop.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
@TestPropertySource("/test.properties")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CartItemRepository cartItemRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private NotificationRepository notificationRepository;

    private OrderRequest orderRequest;
    private User user;
    private Order order;

    @BeforeEach
    void setUp() {

        Set<String> cartItemIds = Set.of("cart-1");

        OrderAddressRequest orderAddressRequest = OrderAddressRequest.builder()
                .contactName("Hieu Duong")
                .phone("0933444555")
                .addressLine1("addressLine1")
                .addressLine2("addressLine2")
                .build();

        orderRequest = OrderRequest.builder()
                .cartItemIds(cartItemIds)
                .orderAddress(orderAddressRequest)
                .build();
        user = User.builder()
                .email("user@test.com")
                .password("password")
                .build();
        Product product = Product.builder()
                .name("Product 1")
                .description("Product 1 description")
                .price(BigDecimal.valueOf(100))
                .quantity(10L)
                .slug("product-1")
                .sku("sku-1")
                .isFeatured(true)
                .isAvailable(true)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(2)
                .price(BigDecimal.valueOf(200))
                .build();
        order = Order.builder()
                .id("order-1")
                .totalQuantity(4)
                .totalPrice(BigDecimal.valueOf(400))
                .orderItems(Set.of(orderItem))
                .user(user)
                .build();
    }


    @Nested
    class HappyCase {

        @Test
        @WithMockUser(username = "user@test.com")
        void getAllOrderByUser_validRequest_success() {
            // GIVEN
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(orderRepository.findAllByUserOrderByLastModifiedDateDesc(any())).thenReturn(List.of(Order.builder().id("order-1").build()));

            // WHEN
            List<OrderResponse> orderResponses = orderService.getAllOrderByUser();

            // THEN
            assertThat(orderResponses.getFirst()).isNotNull()
                    .hasFieldOrPropertyWithValue("id", "order-1");
        }

        @Test
        @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
        void getAllOrders_validRequest_success() {
            // GIVEN
            OrderFilter orderFilter = new OrderFilter();
            orderFilter.setStatus(EOrderStatus.PENDING);

            Pageable pageable = PageRequest.of(0, 5);

            Order order = new Order();
            order.setId("order-1");

            Page<Order> orders = new PageImpl<>(List.of(order));
            when(orderRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(orders);

            // WHEN
            PaginationResponse<OrderAdminResponse> result = orderService.getAllOrders(orderFilter, pageable);

            // THEN
            assertThat(result).isNotNull()
                    .hasFieldOrPropertyWithValue("page", 1)
                    .hasFieldOrPropertyWithValue("total", 1L)
                    .hasFieldOrPropertyWithValue("itemsPerPage", 5)
                    .hasFieldOrPropertyWithValue("data", List.of(OrderAdminResponse.builder().id("order-1").build()));
        }

        @Test
        @WithMockUser(username = "user@test.com")
        void getOrderById_validRequest_success() {
            // GIVEN
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(orderRepository.findByIdAndUser(any(), any())).thenReturn(Optional.of(Order.builder().id("order-1").build()));

            // WHEN
            OrderResponse result = orderService.getOrderById("order-1");

            // THEN
            assertThat(result).isNotNull()
                    .hasFieldOrPropertyWithValue("id", "order-1");
        }

        @Test
        @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
        void getOrderByIdForAdmin_validRequest_success() {
            // GIVEN
            when(orderRepository.findById(any())).thenReturn(Optional.of(Order.builder().id("order-1").build()));

            // WHEN
            OrderAdminResponse result = orderService.getOrderByIdForAdmin("order-1");

            // THEN
            assertThat(result).isNotNull()
                    .hasFieldOrPropertyWithValue("id", "order-1");
        }

        @Test
        @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
        void updateOrderStatus_validRequest_success() {
            // GIVEN
            when(orderRepository.findById(any())).thenReturn(Optional.of(order));
            when(notificationRepository.save(any())).thenReturn(Notification.builder().id("notification-1").build());

            // WHEN
            orderService.updateOrderStatus("order-1", EOrderStatus.COMPLETED.name());

            // THEN
            verify(orderRepository, times(1)).save(any(Order.class));
        }

    }

    @Nested
    class UnHappyCase {
        @Test
        @WithMockUser(username = "user@test.com")
        void create_userNotFound_throwException() {
            // GIVEN
            when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

            // WHEN
            var exception = assertThrows(AppException.class, () -> orderService.create(orderRequest));

            // THEN
            assertThat(exception).isInstanceOf(AppException.class);
            Assertions.assertThat(exception.getErrorCode().getInternalCode())
                    .isEqualTo(1006);

            Assertions.assertThat(exception.getErrorCode().getMessage())
                    .isEqualTo("User not found");
        }

        @Test
        @WithMockUser(username = "user@test.com")
        void create_cartItemNotFound_throwException() {
            // GIVEN
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(cartItemRepository.findAllByUserOrderByLastModifiedDateDesc(any())).thenReturn(List.of());

            // WHEN
            var exception = assertThrows(AppException.class, () -> orderService.create(orderRequest));

            // THEN
            assertThat(exception).isInstanceOf(AppException.class);
            Assertions.assertThat(exception.getErrorCode().getInternalCode())
                    .isEqualTo(1022);

            Assertions.assertThat(exception.getErrorCode().getMessage())
                    .isEqualTo("Cart item not found");
        }

        @Test
        @WithMockUser(username = "user@test.com")
        void getOrderById_orderNotFound_throwException() {
            // GIVEN
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(orderRepository.findByIdAndUser(any(), any())).thenReturn(Optional.empty());

            // WHEN
            var exception = assertThrows(AppException.class, () -> orderService.getOrderById("order-1"));

            // THEN
            assertThat(exception).isInstanceOf(AppException.class);
            Assertions.assertThat(exception.getErrorCode().getInternalCode())
                    .isEqualTo(1023);

            Assertions.assertThat(exception.getErrorCode().getMessage())
                    .isEqualTo("Order not found");
        }
    }

}
