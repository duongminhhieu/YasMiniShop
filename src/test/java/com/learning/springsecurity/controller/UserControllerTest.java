package com.learning.springsecurity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.springsecurity.permission.dto.response.PermissionResponse;
import com.learning.springsecurity.role.dto.response.RoleResponse;
import com.learning.springsecurity.user.UserService;
import com.learning.springsecurity.user.dto.request.UserUpdateRequest;
import com.learning.springsecurity.user.dto.response.UserResponse;
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

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
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
    private List<UserResponse> userResponseList;
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

        userResponseList = Arrays.asList(
                UserResponse.builder()
                        .id("abc-123")
                        .email("user1@gmail.com")
                        .firstName("User")
                        .lastName("One")
                        .build(),
                UserResponse.builder()
                        .id("def-456")
                        .email("user2@gmail.com")
                        .firstName("User")
                        .lastName("Two")
                        .build()
        );

    }

    @Test
    @WithMockUser(username = "duongminhhieu@gmail.com", authorities = {"USER"})
    void getMyInfo_validRequest_success() throws Exception {

        // GIVEN
        when(userService.getMyInfo()).thenReturn(userResponse);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("internalCode").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("abc-123"));
    }

    @Test
    void getMyInfo_unauthenticatedRequest_failure() throws Exception {
        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()
                )
                .andExpect(MockMvcResultMatchers.jsonPath("internalCode").value(1005))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Unauthenticated"));
    }


    @Test
    @WithMockUser(username = "duongminhhieu@gmail.com", authorities = {"ADMIN"})
    void getAllUsers_validRequest_success() throws Exception {
        // GIVEN

        when(userService.getAllUsers()).thenReturn(userResponseList);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("result[0].id").value("abc-123"))
                .andExpect(MockMvcResultMatchers.jsonPath("result[0].email").value("user1@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("result[1].id").value("def-456"))
                .andExpect(MockMvcResultMatchers.jsonPath("result[1].email").value("user2@gmail.com"));
    }

    @Test
    @WithMockUser(username = "duongminhhieu@gmail.com", authorities = {"ADMIN"})
    void deleteUser_validRequest_success() throws Exception {
        // GIVEN

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/abc-123")
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
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/abc-123" )
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
    void getUser_validRequest_success() throws Exception {
        // GIVEN
        when(userService.getUserById("abc-123")).thenReturn(userResponse);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/abc-123")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("internalCode").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("abc-123"));
    }
}
