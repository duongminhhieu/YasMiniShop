package com.learning.yasminishop.service;

import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.common.entity.Product;
import com.learning.yasminishop.common.entity.Rating;
import com.learning.yasminishop.common.entity.User;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.product.ProductRepository;
import com.learning.yasminishop.rating.RatingRepository;
import com.learning.yasminishop.rating.RatingService;
import com.learning.yasminishop.rating.dto.request.RatingRequest;
import com.learning.yasminishop.rating.dto.response.RatingResponse;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Slf4j
@TestPropertySource("/test.properties")
class RatingServiceTest {

    @Autowired
    private RatingService ratingService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private RatingRepository ratingRepository;

    @MockBean
    private UserRepository userRepository;

    private RatingRequest ratingRequest;
    private Product product;
    private User user;
    private Rating rating;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 5);

        ratingRequest = RatingRequest.builder()
                .productId("product-1")
                .star(5)
                .comment("Review")
                .build();

        product = Product.builder()
                .id("product-1")
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

        rating = Rating.builder()
                .id("rating-1")
                .star(5)
                .comment("Review")
                .product(product)
                .user(user)
                .build();

    }

    @Nested
    class HappyCase {

        @Test
        @WithMockUser(username = "user@test.com")
        void create_validRequest_success() {
            // GIVEN
            when(productRepository.findById(anyString())).thenReturn(java.util.Optional.of(product));
            when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(user));
            when(ratingRepository.existsByProductAndUser(any(), any())).thenReturn(false);
            when(ratingRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            // WHEN
            var result = ratingService.create(ratingRequest);

            // THEN
            assertThat(result).isNotNull();
            assertThat(result.getStar()).isEqualTo(ratingRequest.getStar());
            assertThat(result.getComment()).isEqualTo(ratingRequest.getComment());
        }

        @Test
        @WithMockUser(username = "user@test.com")
        void getRatings_validRequest_success() {
            // GIVEN
            List<Rating> ratings = List.of(rating);
            Page<Rating> ratingPage = new PageImpl<>(ratings, pageable, ratings.size());
            when(ratingRepository.findByProductOrderByCreatedDateDesc(any(), any())).thenReturn(ratingPage);
            when(productRepository.findById(anyString())).thenReturn(java.util.Optional.of(product));

            // WHEN
            PaginationResponse<RatingResponse> result = ratingService.getRatings("product-1", pageable);

            // THEN
            Assertions.assertThat(result).isNotNull();
            Assertions.assertThat(result.getData()).isNotEmpty();
            Assertions.assertThat(result.getData().getFirst().getStar()).isEqualTo(rating.getStar());
            Assertions.assertThat(result.getData().getFirst().getComment()).isEqualTo(rating.getComment());
        }

    }

    @Nested
    class UnHappyCase {
        @Test
        @WithMockUser(username = "user@test.com")
        void create_ratingAlreadyExists_exception() {
            // GIVEN
            when(productRepository.findById(anyString())).thenReturn(java.util.Optional.of(product));
            when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(user));
            when(ratingRepository.existsByProductAndUser(any(), any())).thenReturn(true);

            // WHEN
            var exception = assertThrows(AppException.class, () -> ratingService.create(ratingRequest));


            // THEN
            assertThat(exception).isInstanceOf(AppException.class);
            Assertions.assertThat(exception.getErrorCode().getInternalCode())
                    .isEqualTo(1017);

        }

        @Test
        @WithMockUser(username = "user@test.com")
        void create_productNotFound_throwException() {
            // GIVEN
            when(productRepository.findById(anyString())).thenReturn(java.util.Optional.empty());

            // WHEN
            var exception = assertThrows(AppException.class, () -> ratingService.create(ratingRequest));

            // THEN
            assertThat(exception).isInstanceOf(AppException.class);
            Assertions.assertThat(exception.getErrorCode().getInternalCode())
                    .isEqualTo(1009);
        }


    }


}
