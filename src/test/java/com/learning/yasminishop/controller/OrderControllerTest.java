package com.learning.yasminishop.controller;

import com.learning.yasminishop.common.entity.Order;
import com.learning.yasminishop.common.entity.OrderItem;
import com.learning.yasminishop.common.entity.Product;
import com.learning.yasminishop.common.entity.User;
import com.learning.yasminishop.order.OrderRepository;
import com.learning.yasminishop.product.ProductRepository;
import com.learning.yasminishop.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private Order order;
    private User user;
    private Product product;

    @BeforeEach
    void setUp() {

        product = Product.builder()
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

        user = User.builder()
                .email("user@test.com")
                .password("password")
                .build();
        order = Order.builder()
                .id("order-1")
                .totalQuantity(4)
                .totalPrice(BigDecimal.valueOf(400))
                .orderItems(Set.of(orderItem))
                .user(user)
                .build();

    }

    @Test
    @WithMockUser(username = "user@test.com")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getAllOrders_validRequest_success() throws Exception {
        // GIVEN
        User savedUser = userRepository.save(user);
        Product savedProduct = productRepository.save(product);
        order.setUser(savedUser);
        order.getOrderItems().forEach(orderItem -> orderItem.setProduct(savedProduct));
        orderRepository.save(order);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result").isArray());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getOrderById_validRequest_success() throws Exception {
        // GIVEN
        User savedUser = userRepository.save(user);
        Product savedProduct = productRepository.save(product);
        order.setUser(savedUser);
        order.getOrderItems().forEach(orderItem -> orderItem.setProduct(savedProduct));
        Order savedOrder = orderRepository.save(order);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/" + savedOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result.id").value(savedOrder.getId()));
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getAllOrdersForAdmin_validRequest_success() throws Exception {
        // GIVEN
        User savedUser = userRepository.save(user);
        Product savedProduct = productRepository.save(product);
        order.setUser(savedUser);
        order.getOrderItems().forEach(orderItem -> orderItem.setProduct(savedProduct));
        orderRepository.save(order);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result.total").value(1))
                .andExpect(jsonPath("result.page").value(1))
                .andExpect(jsonPath("result.data").isArray());
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getOrderByIdForAdmin_validRequest_success() throws Exception {
        // GIVEN
        User savedUser = userRepository.save(user);
        Product savedProduct = productRepository.save(product);
        order.setUser(savedUser);
        order.getOrderItems().forEach(orderItem -> orderItem.setProduct(savedProduct));
        Order savedOrder = orderRepository.save(order);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/" + savedOrder.getId() + "/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result.id").value(savedOrder.getId()));
    }

}
