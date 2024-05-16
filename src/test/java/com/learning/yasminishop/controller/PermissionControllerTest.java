package com.learning.yasminishop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.yasminishop.permission.PermissionService;
import com.learning.yasminishop.permission.dto.request.PermissionRequest;
import com.learning.yasminishop.permission.dto.response.PermissionResponse;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class PermissionControllerTest {

    @MockBean
    private PermissionService permissionService;

    @Autowired
    private MockMvc mockMvc;

    private PermissionResponse permissionResponse;
    private PermissionRequest permissionRequest;
    private List<PermissionResponse> permissionResponseList;

    @BeforeEach
    void setUp() {
        permissionResponse = PermissionResponse.builder()
                .name("READ")
                .description("Read permission")
                .build();

        permissionRequest = PermissionRequest.builder()
                .name("READ")
                .description("Read permission")
                .build();

        permissionResponseList = List.of(permissionResponse);
    }


    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void create_validRequest_success() throws Exception {

        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String permissionRequestJson = objectMapper.writeValueAsString(permissionRequest);

        when(permissionService.createPermission(any()))
                .thenReturn(permissionResponse);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(permissionRequestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result.name").value("READ"))
                .andExpect(jsonPath("result.description").value("Read permission"));
    }


    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void getAll_validRequest_success() throws Exception {

        // GIVEN
        when(permissionService.getALlPermissions())
                .thenReturn(permissionResponseList);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/permissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000))
                .andExpect(jsonPath("result").isArray())
                .andExpect(jsonPath("result").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void delete_validRequest_success() throws Exception {

        // GIVEN
        String permission = "READ";

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/permissions/" + permission))
                .andExpect(status().isOk())
                .andExpect(jsonPath("internalCode").value(1000));
        verify(permissionService, times(1)).deletePermission(permission);
    }
}
