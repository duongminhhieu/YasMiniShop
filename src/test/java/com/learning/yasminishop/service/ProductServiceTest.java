package com.learning.yasminishop.service;

import com.learning.yasminishop.category.CategoryRepository;
import com.learning.yasminishop.category.dto.response.CategoryResponse;
import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.common.entity.*;
import com.learning.yasminishop.product.ProductRepository;
import com.learning.yasminishop.product.ProductService;
import com.learning.yasminishop.product.dto.filter.ProductFilter;
import com.learning.yasminishop.product.dto.request.ProductRequest;
import com.learning.yasminishop.product.dto.response.ProductAdminResponse;
import com.learning.yasminishop.product.dto.response.ProductResponse;
import com.learning.yasminishop.product.mapper.ProductMapper;
import com.learning.yasminishop.storage.StorageRepository;
import com.learning.yasminishop.storage.dto.response.StorageResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
@Slf4j
@TestPropertySource("/test.properties")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private StorageRepository storageRepository;


    private ProductRequest productCreation;
    private Product product;
    private ProductAdminResponse productAdminResponse;
    private Category category1;
    private Category category2;
    private Storage image1;
    private Storage image2;

    private ProductResponse productResponse;
    private ProductFilter productFilter;

    @BeforeEach
    void setUp() {
        productCreation = ProductRequest.builder()
                .name("Product 1")
                .description("Product 1 description")
                .price(BigDecimal.valueOf(1_000_000))
                .isAvailable(true)
                .isFeatured(true)
                .categoryIds(Set.of("category1", "category2"))
                .slug("product-1")
                .sku("sku-1")
                .imageIds(Set.of("image1", "image2"))
                .build();

        category1 = Category.builder()
                .id("category1")
                .name("Category 1")
                .build();

        category2 = Category.builder()
                .id("category2")
                .name("Category 2")
                .build();
        image1 = Storage.builder()
                .id("image1")
                .url("image1-url")
                .build();

        image2 = Storage.builder()
                .id("image2")
                .url("image2-url")
                .build();

        Set<ProductAttribute> attributes = Set.of(
                ProductAttribute.builder()
                        .id(UUID.randomUUID().toString())
                        .name("Color")
                        .values(Set.of(
                                ProductAttributeValue.builder()
                                        .id(UUID.randomUUID().toString())
                                        .value("Red")
                                        .build()
                        ))
                        .build()
        );

        product = Product.builder()
                .id("product-1")
                .name("Product 1")
                .description("Product 1 description")
                .price(BigDecimal.valueOf(1_000_000))
                .isAvailable(true)
                .isFeatured(true)
                .slug("product-1")
                .sku("sku-1")
                .averageRating(4.0F)
                .quantity(10L)
                .attributes(attributes)
                .images(Set.of(image1, image2))
                .categories(Set.of(category1, category2))
                .build();

        CategoryResponse categoryResponse1 = CategoryResponse.builder()
                .id("category1")
                .name("Category 1")
                .build();
        CategoryResponse categoryResponse2 = CategoryResponse.builder()
                .id("category2")
                .name("Category 2")
                .build();

        StorageResponse storageResponse1 = StorageResponse.builder()
                .id("image1")
                .url("image1-url")
                .build();
        StorageResponse storageResponse2 = StorageResponse.builder()
                .id("image2")
                .url("image2-url")
                .build();

        productAdminResponse = ProductAdminResponse.builder()
                .id("product-1")
                .name("Product 1")
                .description("Product 1 description")
                .price(BigDecimal.valueOf(1_000_000))
                .isAvailable(true)
                .isFeatured(true)
                .slug("product-1")
                .sku("sku-1")
                .images(Set.of(storageResponse1, storageResponse2))
                .categories(Set.of(categoryResponse1, categoryResponse2))
                .build();

        productResponse = ProductResponse.builder()
                .id("product-1")
                .name("Product 1")
                .description("Product 1 description")
                .price(BigDecimal.valueOf(1_000_000))
                .isAvailable(true)
                .isFeatured(true)
                .slug("product-1")
                .sku("sku-1")
                .images(Set.of(storageResponse1, storageResponse2))
                .categories(Set.of(categoryResponse1, categoryResponse2))
                .build();


        productFilter = new ProductFilter();
        productFilter.setCategoryIds(new String[]{"category1", "category2"});

        Category category1 = new Category();
        category1.setId("category1");
        Category category2 = new Category();
        category2.setId("category2");

        Product product = new Product();
        product.setId("product-1");

        ProductAdminResponse productAdminResponse = new ProductAdminResponse();
        productAdminResponse.setId("product-1");

    }


    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void createProduct_validRequest_success() {
        // GIVEN
        when(productRepository.existsBySlug(any())).thenReturn(false);
        when(productRepository.existsBySku(any())).thenReturn(false);
        when(categoryRepository.findAllById(Set.of("category1", "category2"))).thenReturn(List.of(category1, category2));
        when(storageRepository.findAllById(Set.of("image1", "image2"))).thenReturn(List.of(image1, image2));
        when(productMapper.toProduct(any())).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductAdminResponse(product)).thenReturn(productAdminResponse);

        // WHEN
        ProductAdminResponse response = productService.create(productCreation);

        // THEN
        assertThat(response).isNotNull()
                .hasFieldOrPropertyWithValue("id", "product-1");

        verify(categoryRepository).findAllById(Set.of("category1", "category2"));
        verify(storageRepository).findAllById(Set.of("image1", "image2"));
        verify(productRepository).save(product);
    }

    @Test
    void getBySlug_validSlug_success() {
        // GIVEN
        when(productRepository.findBySlug("product-1")).thenReturn(Optional.of(product));
        when(productMapper.toProductResponse(any())).thenReturn(productResponse);

        // WHEN
        ProductResponse response = productService.getBySlug("product-1");

        // THEN
        assertThat(response).isNotNull()
                .hasFieldOrPropertyWithValue("id", "product-1");
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void getById_validId_success() {
        // GIVEN
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productMapper.toProductAdminResponse(any())).thenReturn(productAdminResponse);

        // WHEN
        ProductAdminResponse productAdminResponse1 = productService.getById("product-1");

        // THEN
        assertThat(productAdminResponse1).isNotNull()
                .hasFieldOrPropertyWithValue("id", "product-1");

    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void getAllProductsForAdmin_validRequest_success() {
        // GIVEN

        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productPage = new PageImpl<>(List.of(product));


        when(categoryRepository.findAllById(anyList())).thenReturn(List.of(category1, category2));
        when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(productPage);
        when(productMapper.toProductAdminResponse(any(Product.class))).thenReturn(productAdminResponse);

        // WHEN
        PaginationResponse<ProductAdminResponse> response = productService.getAllProductsForAdmin(productFilter, pageable);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getData().getFirst().getId()).isEqualTo("product-1");

        verify(categoryRepository).findAllById(anyList());
        verify(productRepository).findAll(any(Specification.class), eq(pageable));
        verify(productMapper, times(1)).toProductAdminResponse(any(Product.class));
    }


}
