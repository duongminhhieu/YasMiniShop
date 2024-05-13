package com.learning.springsecurity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.springsecurity.permission.dto.response.PermissionResponse;
import com.learning.springsecurity.role.RoleService;
import com.learning.springsecurity.role.dto.request.RoleRequest;
import com.learning.springsecurity.role.dto.response.RoleResponse;
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

import java.util.List;
import java.util.Set;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    private RoleResponse roleResponse;
    private RoleRequest roleRequest;
    private List<RoleResponse> roleResponseList;

    @BeforeEach
    void setUp() {

        roleRequest = RoleRequest.builder()
                .name("USER")
                .description("User role")
                .permissions(Set.of("READ"))
                .build();

        PermissionResponse permissionResponse = PermissionResponse.builder()
                .name("READ")
                .description("Read permission")
                .build();

        roleResponse = RoleResponse.builder()
                .name("USER")
                .description("User role")
                .permissions(Set.of(permissionResponse))
                .build();

        roleResponseList = List.of(roleResponse);

    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ADMIN"})
    void createRole_validRequest_success() throws Exception{
        // GIVEN

        ObjectMapper objectMapper = new ObjectMapper();
        String roleRequestJson = objectMapper.writeValueAsString(roleRequest);
        when(roleService.create(any(RoleRequest.class))).thenReturn(roleResponse);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(roleRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.internalCode").value(1000))
                .andExpect(jsonPath("$.result.name").value("USER"))
                .andExpect(jsonPath("$.result.description").value("User role"))
                .andExpect(jsonPath("$.result.permissions").isArray());

    }


    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ADMIN"})
    void getAll_validRequest_success() throws Exception {
        // GIVEN

        when(roleService.getAll()).thenReturn(roleResponseList);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/roles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.internalCode").value(1000))
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result").isNotEmpty());

    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ADMIN"})
    void deleteRole_validRequest_success() throws Exception {
        // GIVEN

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/roles/USER")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.internalCode").value(1000))
                .andExpect(jsonPath("$.result").value("Role deleted successfully"));

        verify(roleService, times(1)).delete("USER");

    }



}
