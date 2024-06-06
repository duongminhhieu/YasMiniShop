package com.learning.yasminishop.service;

import com.learning.yasminishop.cart.CartItemRepository;
import com.learning.yasminishop.cart.CartItemService;
import com.learning.yasminishop.cart.dto.request.CartItemRequest;
import com.learning.yasminishop.cart.dto.request.CartItemUpdate;
import com.learning.yasminishop.cart.dto.response.CartItemResponse;
import com.learning.yasminishop.common.entity.CartItem;
import com.learning.yasminishop.common.entity.Product;
import com.learning.yasminishop.common.entity.User;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.product.ProductRepository;
import com.learning.yasminishop.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Slf4j
@TestPropertySource("/test.properties")
class CartServiceTest {

    @MockBean
    private CartItemRepository cartItemRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private CartItemService cartItemService;


    private CartItemRequest cartItemRequest;
    private Product product;
    private User user;
    private CartItem cartItem;
    private CartItemUpdate cartItemUpdate;
    private List<String> cartIds;
    private List<CartItem> cartItems;

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
                .email("user@test.com")
                .password("password")
                .build();
        cartItem = CartItem.builder()
                .product(product)
                .quantity(2)
                .user(user)
                .price(BigDecimal.valueOf(200))
                .build();

        cartItemUpdate = CartItemUpdate.builder()
                .quantity(3)
                .build();
        cartIds = List.of("cart-1", "cart-2");
        cartItems = List.of(CartItem.builder()
                        .id("cart-1")
                        .product(product)
                        .quantity(2)
                        .user(user)
                        .price(BigDecimal.valueOf(200))
                        .build(),
                CartItem.builder()
                        .id("cart-2")
                        .product(product)
                        .quantity(2)
                        .user(user)
                        .price(BigDecimal.valueOf(200))
                        .build());
    }

    @Nested
    class HappyCase {

        @Test
        @WithMockUser(username = "user@test.com")
        void create_validRequest_success() {
            // GIVEN
            when(productRepository.findById(cartItemRequest.getProductId())).thenReturn(Optional.of(product));
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(cartItemRepository.findByProductAndUser(product, user)).thenReturn(Optional.empty());
            when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

            // WHEN
            CartItemResponse result = cartItemService.create(cartItemRequest);

            // THEN
            assertEquals(cartItem.getQuantity(), result.getQuantity());
            assertEquals(cartItem.getPrice(), result.getPrice());
            assertEquals(cartItem.getProduct().getId(), result.getProduct().getId());
        }

        @Test
        @WithMockUser(username = "user@test.com")
        void getAll_validRequest_success() {
            // GIVEN
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(cartItemRepository.findAllByUserOrderByLastModifiedDateDesc(user)).thenReturn(java.util.List.of(cartItem));

            // WHEN
            var result = cartItemService.getAll();

            // THEN
            assertThat(result).isNotNull()
                    .hasSize(1)
                    .first()
                    .hasFieldOrPropertyWithValue("quantity", 2)
                    .hasFieldOrPropertyWithValue("price", BigDecimal.valueOf(200));
        }

        @Test
        @WithMockUser(username = "user@test.com")
        void update_validRequest_success() {
            // GIVEN
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(cartItemRepository.findById(cartItem.getId())).thenReturn(Optional.of(cartItem));
            when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

            // WHEN
            var result = cartItemService.update(cartItem.getId(), cartItemUpdate);

            // THEN
            assertEquals(cartItemUpdate.getQuantity(), result.getQuantity());
        }

        @Test
        @WithMockUser(username = "user@test.com")
        void delete_validRequest_success() {
            // GIVEN
            when(cartItemRepository.findAllById(cartIds)).thenReturn(cartItems);
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

            // WHEN
            cartItemService.delete(cartIds);

            // THEN
            verify(cartItemRepository).deleteAll(cartItems);
        }

        @Test
        @WithMockUser(username = "user@test.com")
        void getCartByIds_validRequest_success() {
            // GIVEN
            when(cartItemRepository.findAllById(cartIds)).thenReturn(cartItems);

            // WHEN
            var result = cartItemService.getCartByIds(cartIds);

            // THEN
            assertThat(result).isNotNull()
                    .hasSize(2)
                    .first()
                    .hasFieldOrPropertyWithValue("id", "cart-1")
                    .hasFieldOrPropertyWithValue("quantity", 2);
        }

    }

    @Nested
    class UnhappyCase {

        @Test
        @WithMockUser(username = "user@test.com")
        void create_productNotFound_throwException() {
            // GIVEN
            when(productRepository.findById(cartItemRequest.getProductId())).thenReturn(Optional.empty());

            // WHEN
            var exception = assertThrows(AppException.class, () -> cartItemService.create(cartItemRequest));

            // THEN
            assertThat(exception.getErrorCode().getInternalCode())
                    .isEqualTo(1009);
            assertThat(exception.getErrorCode().getMessage())
                    .isEqualTo("Product not found");
        }

    }


}
