package com.learning.yasminishop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.rating.RatingService;
import com.learning.yasminishop.rating.dto.request.RatingRequest;
import com.learning.yasminishop.rating.dto.response.RatingResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class RatingControllerTest {

    @MockBean
    private RatingService ratingService;

    @Autowired
    private MockMvc mockMvc;

    private RatingRequest ratingRequest;
    private RatingResponse ratingResponse;
    private PaginationResponse<RatingResponse> paginationResponse;

    @BeforeEach
    void setUp() {
        ratingRequest = RatingRequest.builder()
                .productId("product-1")
                .star(5)
                .comment("This is a great product!")
                .build();

        ratingResponse = RatingResponse.builder()
                .id("rating-1")
                .star(5)
                .comment("This is a great product!")
                .build();

        paginationResponse = PaginationResponse.<RatingResponse>builder()
                .data(List.of(ratingResponse))
                .total(1L)
                .page(1)
                .itemsPerPage(10)
                .build();
    }

    @Nested
    class HappyCase {

        @Test
        @WithMockUser(username = "user@test.com", roles = {"USER"})
        void createRating_validRequest_success() throws Exception {
            // GIVEN
            ObjectMapper objectMapper = new ObjectMapper();
            String requestJson = objectMapper.writeValueAsString(ratingRequest);
            when(ratingService.create(any(RatingRequest.class))).thenReturn(ratingResponse);

            // WHEN THEN
            mockMvc.perform(MockMvcRequestBuilders.post("/rating")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("internalCode").value(1000))
                    .andExpect(jsonPath("result.id").value("rating-1"))
                    .andExpect(jsonPath("result.star").value(5))
                    .andExpect(jsonPath("result.comment").value("This is a great product!"));
        }

        @Test
        @WithMockUser(username = "user@test.com", roles = {"USER"})
        void getRatings_validRequest_success() throws Exception {
            // GIVEN
            when(ratingService.getRatings(anyString(), any())).thenReturn(paginationResponse);

            // WHEN THEN
            mockMvc.perform(MockMvcRequestBuilders.get("/rating")
                            .param("productId", "product-1")
                            .param("page", "1")
                            .param("itemsPerPage", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("internalCode").value(1000))
                    .andExpect(jsonPath("result.data").isArray())
                    .andExpect(jsonPath("result.data").isNotEmpty())
                    .andExpect(jsonPath("result.total").value(1))
                    .andExpect(jsonPath("result.page").value(1))
                    .andExpect(jsonPath("result.itemsPerPage").value(10));
        }

    }

    @Nested
    class UnHappyCase {

        @Test
        @WithMockUser(username = "user@test.com", roles = {"USER"})
        void createRating_invalidStar_badRequest() throws Exception {
            // GIVEN
            ObjectMapper objectMapper = new ObjectMapper();
            RatingRequest invalidRequest = RatingRequest.builder()
                    .productId("product-1")
                    .star(6)
                    .comment("This is a great product!")
                    .build();
            String requestJson = objectMapper.writeValueAsString(invalidRequest);

            // WHEN THEN
            mockMvc.perform(MockMvcRequestBuilders.post("/rating")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("internalCode").value(2009));
        }

        @Test
        @WithMockUser(username = "user@test.com", roles = {"USER"})
        void createRating_commentTooShort_badRequest() throws Exception {
            // GIVEN
            ObjectMapper objectMapper = new ObjectMapper();
            RatingRequest invalidRequest = RatingRequest.builder()
                    .productId("product-1")
                    .star(5)
                    .comment("Short")
                    .build();
            String requestJson = objectMapper.writeValueAsString(invalidRequest);

            // WHEN THEN
            mockMvc.perform(MockMvcRequestBuilders.post("/rating")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("internalCode").value(2010));
        }

        @Test
        @WithMockUser(username = "user@test.com", roles = {"USER"})
        void getRatings_invalidPage_badRequest() throws Exception {
            // WHEN THEN
            mockMvc.perform(MockMvcRequestBuilders.get("/rating")
                            .param("productId", "product-1")
                            .param("page", "0")
                            .param("itemsPerPage", "10"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("internalCode").value(1016));
        }

    }

}
