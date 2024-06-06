package com.learning.yasminishop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.yasminishop.cart.CartItemRepository;
import com.learning.yasminishop.cart.dto.request.CartItemRequest;
import com.learning.yasminishop.common.entity.CartItem;
import com.learning.yasminishop.common.entity.Product;
import com.learning.yasminishop.common.entity.User;
import com.learning.yasminishop.product.ProductRepository;
import com.learning.yasminishop.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    private CartItemRequest cartItemRequest;
    private Product product;
    private User user;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        cartItemRequest = CartItemRequest.builder()
                .productId("product-1")
                .quantity(2)
                .build();
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
        user = User.builder()
                .email("user@gmail.com")
                .password("password")
                .build();
        cartItem = CartItem.builder()
                .product(product)
                .quantity(2)
                .price(BigDecimal.valueOf(200))
                .build();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @WithMockUser(username = "user@gmail.com")
    void createCart_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        Product savedProduct = productRepository.save(product);
        userRepository.save(user);
        cartItemRequest.setProductId(savedProduct.getId());
        String requestJson = objectMapper.writeValueAsString(cartItemRequest);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result.product.name").value("Product 1"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @WithMockUser(username = "user@gmail.com")
    void getAllCarts_validRequest_success() throws Exception {
        // GIVEN
        Product savedProduct = productRepository.save(product);
        User savedUser = userRepository.save(user);
        cartItem.setProduct(savedProduct);
        cartItem.setUser(savedUser);
        cartItemRepository.save(cartItem);


        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/carts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result[0].product.name").value("Product 1"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @WithMockUser(username = "user@gmail.com")
    void updateCart_validRequest_success() throws Exception {
        // GIVEN
        Product savedProduct = productRepository.save(product);
        User savedUser = userRepository.save(user);
        cartItem.setProduct(savedProduct);
        cartItem.setUser(savedUser);
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        String requestJson = "{\"quantity\": 5}";

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.put("/carts/" + savedCartItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result.quantity").value(5));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @WithMockUser(username = "user@gmail.com")
    void deleteCart_validRequest_success() throws Exception {
        // GIVEN
        Product savedProduct = productRepository.save(product);
        User savedUser = userRepository.save(user);
        cartItem.setProduct(savedProduct);
        cartItem.setUser(savedUser);
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        String requestJson = "{\"ids\": [\"" + savedCartItem.getId() + "\"]}";

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("message").value("Cart items deleted successfully"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @WithMockUser(username = "user@gmail.com")
    void getCartItemsByIds_validRequest_success() throws Exception {
        // GIVEN
        Product savedProduct = productRepository.save(product);
        User savedUser = userRepository.save(user);
        cartItem.setProduct(savedProduct);
        cartItem.setUser(savedUser);
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/carts/get-by-ids")
                        .param("ids", savedCartItem.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result[0].product.name").value("Product 1"));
    }

}
