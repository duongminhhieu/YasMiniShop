package com.learning.yasminishop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.yasminishop.category.CategoryRepository;
import com.learning.yasminishop.category.dto.request.CategoryCreation;
import com.learning.yasminishop.category.dto.request.CategoryIds;
import com.learning.yasminishop.common.entity.Category;
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

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    private CategoryCreation categoryCreation;
    private Category category;
    private CategoryIds categoryIds;

    @BeforeEach
    void setUp() {

        categoryCreation = CategoryCreation.builder()
                .name("Category Create")
                .description("Category create description")
                .slug("category-create")
                .build();
        category = Category.builder()
                .name("Category 1")
                .description("Category 1 description")
                .slug("category-1")
                .isAvailable(true)
                .build();

        categoryIds = CategoryIds.builder()
                .ids(List.of("category-1"))
                .build();

    }


    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void createCategory_validRequest_success() throws Exception {

        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String categoryCreationJson = objectMapper.writeValueAsString(categoryCreation);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryCreationJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result.name").value("Category Create"))
                .andExpect(jsonPath("result.description").value("Category create description"))
                .andExpect(jsonPath("result.slug").value("category-create"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getAllCategories_validRequest_success() throws Exception {
        // GIVEN
        categoryRepository.save(category);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result").isArray());
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getCategoryBySlug_validRequest_success() throws Exception {
        // GIVEN
        categoryRepository.save(category);


        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/slug/category-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result.name").value("Category 1"))
                .andExpect(jsonPath("result.description").value("Category 1 description"))
                .andExpect(jsonPath("result.slug").value("category-1"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getCategoryById_validRequest_success() throws Exception {
        // GIVEN
        Category saved = categoryRepository.save(category);
        String id = saved.getId();

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result.id").value(id))
                .andExpect(jsonPath("result.name").value("Category 1"))
                .andExpect(jsonPath("result.description").value("Category 1 description"))
                .andExpect(jsonPath("result.slug").value("category-1"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void toggleAvailability_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        Category saved = categoryRepository.save(category);

        categoryIds.setIds(List.of(saved.getId()));
        String idsJson = objectMapper.writeValueAsString(categoryIds);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.patch("/categories/toggle-availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(idsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("message").value("Categories availability toggled successfully"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void deleteCategories_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        Category saved = categoryRepository.save(category);

        categoryIds.setIds(List.of(saved.getId()));
        String idsJson = objectMapper.writeValueAsString(categoryIds);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(idsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("message").value("Category deleted successfully"));
    }


    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getAllCategoriesForAdmin_validRequest_success() throws Exception {
        // GIVEN
        String name = "Category";
        boolean isAvailable = true;
        int page = 1;
        int itemsPerPage = 10;

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/admin")
                        .param("name", name)
                        .param("isAvailable", Boolean.toString(isAvailable))
                        .param("page", Integer.toString(page))
                        .param("itemsPerPage", Integer.toString(itemsPerPage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result").exists());
    }


}
