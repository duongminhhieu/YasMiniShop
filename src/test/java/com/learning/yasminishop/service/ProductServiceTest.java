package com.learning.yasminishop.service;

import com.learning.yasminishop.product.ProductRepository;
import com.learning.yasminishop.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@Slf4j
@TestPropertySource("/test.properties")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;



    @BeforeEach
    void setUp() {

    }


    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void createProduct_validRequest_success() {
        // GIVEN

        // WHEN

        // THEN
    }


}
