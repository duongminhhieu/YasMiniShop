package com.learning.yasminishop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.permission.dto.response.PermissionResponse;
import com.learning.yasminishop.role.dto.response.RoleResponse;
import com.learning.yasminishop.user.UserService;
import com.learning.yasminishop.user.dto.request.UserUpdateRequest;
import com.learning.yasminishop.user.dto.response.UserAdminResponse;
import com.learning.yasminishop.user.dto.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserResponse userResponse;
    private UserUpdateRequest userUpdateRequest;
    private PaginationResponse<UserAdminResponse> userResponseList;
    private Set<RoleResponse> roleResponseList;


    @BeforeEach
    void setUp() {

        roleResponseList = Set.of(
                RoleResponse.builder()
                        .name("USER")
                        .description("User role")
                        .permissions(Set.of(
                                PermissionResponse.builder()
                                        .name("READ")
                                        .build()
                        ))
                        .build(),
                RoleResponse.builder()
                        .name("ADMIN")
                        .description("Admin role")
                        .permissions(Set.of(
                                PermissionResponse.builder()
                                        .name("READ")
                                        .build(),
                                PermissionResponse.builder()
                                        .name("WRITE")
                                        .build(),
                                PermissionResponse.builder()
                                        .name("DELETE")
                                        .build(),
                                PermissionResponse.builder()
                                        .name("UPDATE")
                                        .build()
                        ))
                        .build()
        );

        userResponse = UserResponse.builder()
                .id("abc-123")
                .email("duongminhhieu@gmail.com")
                .firstName("Hieu")
                .lastName("Duong")
                .build();

        userUpdateRequest = UserUpdateRequest.builder()
                .firstName("Hieu")
                .lastName("Duong")
                .password("123456")
                .roles(Set.of("USER", "ADMIN"))
                .build();

        userResponseList = PaginationResponse.<UserAdminResponse>builder()
                .page(1)
                .total(2L)
                .itemsPerPage(10)
                .data(Arrays.asList(
                        UserAdminResponse.builder()
                                .id("abc-123")
                                .email("user1@gmail.com")
                                .firstName("User")
                                .lastName("One")
                                .build(),
                        UserAdminResponse.builder()
                                .id("def-456")
                                .email("user2@gmail.com")
                                .firstName("User")
                                .lastName("Two")
                                .build()
                ))
                .build();

    }

    @Test
    @WithMockUser(username = "duongminhhieu@gmail.com", authorities = {"USER"})
    void getMyInfo_validRequest_success() throws Exception {

        // GIVEN
        when(userService.getMyInfo()).thenReturn(userResponse);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("internalCode").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("abc-123"));
    }

    @Test
    void getMyInfo_unauthenticatedRequest_failure() throws Exception {
        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()
                )
                .andExpect(MockMvcResultMatchers.jsonPath("internalCode").value(1005))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Unauthenticated"));
    }


    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void getAllUsers_validRequest_success() throws Exception {
        // GIVEN

        when(userService.getAllUsers(any())).thenReturn(userResponseList);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result.data", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "duongminhhieu@gmail.com", roles = {"ADMIN"})
    void deleteUser_validRequest_success() throws Exception {
        // GIVEN

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/abc-123")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("internalCode").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("User deleted successfully"));
    }

    @Test
    @WithMockUser(username = "duongminhhieu@gmail.com", authorities = {"UPDATE_DATA"})
    void updateUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String userUpdateRequestJson = objectMapper.writeValueAsString(userUpdateRequest);
        userResponse.setRoles(roleResponseList);
        when(userService.updateUser(ArgumentMatchers.anyString(), ArgumentMatchers.any(UserUpdateRequest.class)))
                .thenReturn(userResponse);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.put("/users/abc-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUpdateRequestJson)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("internalCode").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.firstName").value("Hieu"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.lastName").value("Duong"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.roles").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("result.roles", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("result.roles[*].name", hasItem("USER")))
                .andExpect(MockMvcResultMatchers.jsonPath("result.roles[*].name", hasItem("ADMIN")));
    }


    @Test
    @WithMockUser(username = "duongminhhieu@gmail.com", authorities = {"UPDATE_DATA"})
    void updateUser_emptyFirstName_fail() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        userUpdateRequest.setFirstName("");

        String userUpdateRequestJson = objectMapper.writeValueAsString(userUpdateRequest);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.put("/users/abc-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUpdateRequestJson)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("internalCode").value(2003))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("\"firstName\" must not be empty"))
        ;
    }

    @Test
    @WithMockUser(username = "duongminhhieu@gmail.com", authorities = {"UPDATE_DATA"})
    void updateUser_emptyRole_fail() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        userUpdateRequest.setRoles(Set.of());

        String userUpdateRequestJson = objectMapper.writeValueAsString(userUpdateRequest);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.put("/users/abc-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUpdateRequestJson)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("internalCode").value(2003))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("\"roles\" must not be empty"))
        ;
    }

    @Test
    @WithMockUser(username = "duongminhhieu@gmail.com", authorities = {"UPDATE_DATA"})
    void updateUser_passwordInvalid_fail() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        userUpdateRequest.setPassword("12345");

        String userUpdateRequestJson = objectMapper.writeValueAsString(userUpdateRequest);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.put("/users/abc-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUpdateRequestJson)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("internalCode").value(2001))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Password must be at least 6 characters"))
        ;
    }

    @Test
    @WithMockUser(username = "duongminhhieu@gmail.com", authorities = {"UPDATE_DATA"})
    void updateUser_dobInvalid_fail() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        userUpdateRequest.setDob(LocalDate.now());
        objectMapper.registerModule(new JavaTimeModule());
        String userUpdateRequestJson = objectMapper.writeValueAsString(userUpdateRequest);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.put("/users/abc-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUpdateRequestJson)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("internalCode").value(2005))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Your age must be at least 18"))
        ;
    }


    @Test
    @WithMockUser(username = "admin@local.com", roles = {"ADMIN"})
    void getUser_validRequest_success() throws Exception {
        // GIVEN
        when(userService.getUserById("abc-123")).thenReturn(userResponse);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/users/abc-123")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("internalCode").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("abc-123"));
    }
}
