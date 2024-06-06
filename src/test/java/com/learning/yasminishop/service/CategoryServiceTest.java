package com.learning.yasminishop.service;

import com.learning.yasminishop.category.CategoryRepository;
import com.learning.yasminishop.category.CategoryService;
import com.learning.yasminishop.category.dto.request.CategoryCreation;
import com.learning.yasminishop.category.dto.request.CategoryUpdate;
import com.learning.yasminishop.category.dto.response.CategoryResponse;
import com.learning.yasminishop.common.entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
@TestPropertySource("/test.properties")
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    private CategoryCreation categoryCreation;

    private Category category;
    private CategoryUpdate categoryUpdate;

    @BeforeEach
    void setUp() {

        categoryCreation = CategoryCreation.builder()
                .name("Category Create")
                .description("Category create description")
                .slug("category-create")
                .build();

        category = Category.builder()
                .id("cate1")
                .name("Category 1")
                .description("Category 1 description")
                .slug("category-1")
                .isAvailable(true)
                .products(Set.of())
                .build();

        categoryUpdate = CategoryUpdate.builder()
                .name("Category update")
                .description("Category 1 update description")
                .slug("category-1")
                .isAvailable(true)
                .build();

    }

    @Nested
    class HappyCase {

        @Test
        @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
        void createCategory_validRequest_success() {
            // GIVEN
            when(categoryRepository.existsBySlug(categoryCreation.getSlug())).thenReturn(false);
            when(categoryRepository.save(any(Category.class))).thenReturn(category);

            // WHEN
            CategoryResponse response = categoryService.create(categoryCreation);

            // THEN
            assertThat(response).isNotNull()
                    .hasFieldOrPropertyWithValue("name", "Category 1")
                    .hasFieldOrPropertyWithValue("description", "Category 1 description")
                    .hasFieldOrPropertyWithValue("slug", "category-1");
        }

        @Test
        void getAllCategories_validRequest_success() {
            // GIVEN
            when(categoryRepository.findAllByIsAvailable(true)).thenReturn(List.of(category));

            // WHEN
            List<CategoryResponse> response = categoryService.getAllCategories();

            // THEN
            assertThat(response).isNotNull()
                    .hasSize(1)
                    .first()
                    .hasFieldOrPropertyWithValue("name", "Category 1")
                    .hasFieldOrPropertyWithValue("description", "Category 1 description")
                    .hasFieldOrPropertyWithValue("slug", "category-1");
        }

        @Test
        void getBySlug_validRequest_success() {
            // GIVEN
            when(categoryRepository.findBySlug("category-1")).thenReturn(Optional.of(category));

            // WHEN
            CategoryResponse response = categoryService.getBySlug("category-1");

            // THEN
            assertThat(response).isNotNull()
                    .hasFieldOrPropertyWithValue("name", "Category 1")
                    .hasFieldOrPropertyWithValue("description", "Category 1 description")
                    .hasFieldOrPropertyWithValue("slug", "category-1");
        }

        @Test
        @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
        void getCategory_validRequest_success() {
            // GIVEN
            when(categoryRepository.findById("cate1")).thenReturn(Optional.of(category));

            // WHEN
            var response = categoryService.getCategory("cate1");

            // THEN
            assertThat(response).isNotNull()
                    .hasFieldOrPropertyWithValue("name", "Category 1")
                    .hasFieldOrPropertyWithValue("description", "Category 1 description")
                    .hasFieldOrPropertyWithValue("slug", "category-1");
        }

        @Test
        @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
        void delete_validRequest_success() {
            // GIVEN
            when(categoryRepository.findAllById(List.of("cate1"))).thenReturn(List.of(category));

            // WHEN
            categoryService.delete(List.of("cate1"));

            // THEN
            verify(categoryRepository).deleteAll(List.of(category));
        }

        @Test
        @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
        void toggleAvailability_validRequest_success() {
            // GIVEN
            when(categoryRepository.findAllById(List.of("cate1"))).thenReturn(List.of(category));

            // WHEN
            categoryService.toggleAvailability(List.of("cate1"));

            // THEN
            verify(categoryRepository).saveAll(List.of(category));
        }

        @Test
        @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
        void update_validRequest_success() {
            // GIVEN
            when(categoryRepository.findById("cate1")).thenReturn(Optional.of(category));
            when(categoryRepository.save(any(Category.class))).thenReturn(category);

            // WHEN
            var response = categoryService.update("cate1", categoryUpdate);

            // THEN
            assertThat(response).isNotNull()
                    .hasFieldOrPropertyWithValue("name", "Category update");
        }
    }
}


