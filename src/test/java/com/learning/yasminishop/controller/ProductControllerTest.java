package com.learning.yasminishop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.yasminishop.category.dto.response.CategoryResponse;
import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.product.ProductService;
import com.learning.yasminishop.product.dto.request.ProductAttributeRequest;
import com.learning.yasminishop.product.dto.request.ProductAttributeValueRequest;
import com.learning.yasminishop.product.dto.request.ProductIds;
import com.learning.yasminishop.product.dto.request.ProductRequest;
import com.learning.yasminishop.product.dto.response.ProductAdminResponse;
import com.learning.yasminishop.product.dto.response.ProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    private ProductRequest productCreation;
    private ProductResponse productResponse;
    private ProductAdminResponse productAdminResponse;
    private PaginationResponse<ProductAdminResponse> productAdminResponsePaginationResponse;
    private ProductRequest productUpdate;
    private ProductIds productIds;


    @BeforeEach
    void setUp() {

        productIds = ProductIds.builder()
                .ids(List.of("1", "2"))
                .build();

        ProductAttributeRequest productAttributeRequest = ProductAttributeRequest.builder()
                .name("Attribute 1")
                .values(List.of(new ProductAttributeValueRequest("Value 1"), new ProductAttributeValueRequest("Value 1")))
                .build();

        productCreation = ProductRequest.builder()
                .name("Product 1")
                .description("Product 1 description")
                .price(BigDecimal.valueOf(1_000_000))
                .sku("SKU-1")
                .slug("product-1")
                .quantity(10L)
                .isFeatured(true)
                .isAvailable(true)
                .categoryIds(Set.of("1", "2"))
                .imageIds(Set.of("1", "2"))
                .attributes(List.of(productAttributeRequest))
                .build();

        Set<CategoryResponse> categoryResponseSet = Set.of(CategoryResponse.builder()
                        .id("1")
                        .name("Category 1")
                        .slug("category-1")
                        .description("Category 1 description")
                        .build(),
                CategoryResponse.builder()
                        .id("2")
                        .name("Category 2")
                        .slug("category-2")
                        .description("Category 2 description")
                        .build()
        );

        productResponse = ProductResponse.builder()
                .name("Product 1")
                .description("Product 1 description")
                .price(BigDecimal.valueOf(1_000_000))
                .sku("SKU-1")
                .slug("product-1")
                .quantity(10L)
                .isFeatured(true)
                .categories(categoryResponseSet)
                .build();

        productAdminResponse = ProductAdminResponse.builder()
                .name("Product 1")
                .description("Product 1 description")
                .price(BigDecimal.valueOf(1_000_000))
                .sku("SKU-1")
                .slug("product-1")
                .quantity(10L)
                .isFeatured(true)
                .categories(categoryResponseSet)
                .build();

        productAdminResponsePaginationResponse = PaginationResponse.<ProductAdminResponse>builder()
                .data(List.of(productAdminResponse))
                .total(1L)
                .page(1)
                .itemsPerPage(10)
                .build();

        productUpdate = ProductRequest.builder()
                .name("Product 1")
                .description("Product 1 description")
                .price(BigDecimal.valueOf(1_000_000))
                .sku("SKU-1")
                .slug("product-1")
                .quantity(10L)
                .isFeatured(true)
                .isAvailable(true)
                .attributes(List.of(productAttributeRequest))
                .categoryIds(Set.of("1", "2"))
                .imageIds(Set.of("1", "2"))
                .build();
    }


    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void createProduct_validRequest_success() throws Exception {

        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String productCreationJson = objectMapper.writeValueAsString(productCreation);
        when(productService.create(any(ProductRequest.class))).thenReturn(productAdminResponse);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productCreationJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result.name").value("Product 1"))
                .andExpect(jsonPath("result.description").value("Product 1 description"))
                .andExpect(jsonPath("result.price").value(1_000_000))
                .andExpect(jsonPath("result.sku").value("SKU-1"))
                .andExpect(jsonPath("result.slug").value("product-1"))
                .andExpect(jsonPath("result.quantity").value(10))
                .andExpect(jsonPath("result.isFeatured").value(true))
                .andExpect(jsonPath("result.categories").isArray())
                .andExpect(jsonPath("result.categories").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void getBySlug_validRequest_success() throws Exception {

        // GIVEN
        String slug = "product-1";
        when(productService.getBySlug(slug)).thenReturn(productResponse);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/products/" + slug))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result.name").value("Product 1"))
                .andExpect(jsonPath("result.description").value("Product 1 description"))
                .andExpect(jsonPath("result.price").value(1_000_000))
                .andExpect(jsonPath("result.sku").value("SKU-1"))
                .andExpect(jsonPath("result.slug").value("product-1"))
                .andExpect(jsonPath("result.quantity").value(10))
                .andExpect(jsonPath("result.isFeatured").value(true))
                .andExpect(jsonPath("result.categories").isArray())
                .andExpect(jsonPath("result.categories").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void getById_validRequest_success() throws Exception {

        // GIVEN
        String id = "1";
        when(productService.getById(id)).thenReturn(productAdminResponse);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/products/id/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result.name").value("Product 1"))
                .andExpect(jsonPath("result.description").value("Product 1 description"))
                .andExpect(jsonPath("result.price").value(1_000_000))
                .andExpect(jsonPath("result.sku").value("SKU-1"))
                .andExpect(jsonPath("result.slug").value("product-1"))
                .andExpect(jsonPath("result.quantity").value(10))
                .andExpect(jsonPath("result.isFeatured").value(true))
                .andExpect(jsonPath("result.categories").isArray())
                .andExpect(jsonPath("result.categories").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void getAllForAdmin_validRequest_success() throws Exception {

        // GIVEN
        when(productService.getAllProductsForAdmin(any(), any(), any(), any(), any())).thenReturn(productAdminResponsePaginationResponse);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/products/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", "Product 1")
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




    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void updateProduct_validRequest_success() throws Exception {

        // GIVEN
        String id = "1";
        ObjectMapper objectMapper = new ObjectMapper();
        String productUpdateJson = objectMapper.writeValueAsString(productUpdate);
        when(productService.update(id, productUpdate)).thenReturn(productAdminResponse);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.put("/products/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productUpdateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result.name").value("Product 1"))
                .andExpect(jsonPath("result.description").value("Product 1 description"))
                .andExpect(jsonPath("result.price").value(1_000_000))
                .andExpect(jsonPath("result.sku").value("SKU-1"))
                .andExpect(jsonPath("result.slug").value("product-1"))
                .andExpect(jsonPath("result.quantity").value(10))
                .andExpect(jsonPath("result.isFeatured").value(true))
                .andExpect(jsonPath("result.categories").isArray())
                .andExpect(jsonPath("result.categories").isNotEmpty());
    }


    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void deleteProduct_validRequest_success() throws Exception {

        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String productIdsJson = objectMapper.writeValueAsString(productIds);


        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/products" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productIdsJson))

                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000));
    }


}
