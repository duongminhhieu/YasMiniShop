package com.learning.springsecurity.service;


import com.learning.springsecurity.common.entity.Permission;
import com.learning.springsecurity.permission.PermissionRepository;
import com.learning.springsecurity.permission.PermissionService;
import com.learning.springsecurity.permission.dto.request.PermissionRequest;
import com.learning.springsecurity.permission.dto.response.PermissionResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
class PermissionServiceTest {

    @MockBean
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionService permissionService;

    private PermissionResponse permissionResponse;
    private PermissionRequest permissionRequest;
    private List<PermissionResponse> permissionResponseList;
    private Permission permission;

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
        permission = Permission.builder()
                .name("READ")
                .description("Read permission")
                .build();
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void createPermission_validRequest_success() {
        // given
        when(permissionRepository.save(any(Permission.class))).thenReturn(permission);

        // when
        PermissionResponse response = permissionService.createPermission(permissionRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("READ");
        assertThat(response.getDescription()).isEqualTo("Read permission");
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void getAllPermissions_validRequest_success() {
        // given
        when(permissionRepository.findAll()).thenReturn(List.of(permission));

        // when
        List<PermissionResponse> response = permissionService.getALlPermissions();

        // then
        assertThat(response).isNotNull()
                .isNotEmpty()
                .hasSize(1);

    }

 @Test
@WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
void deletePermission_validRequest_success() {
    // given
    String permissionName = "READ";
    doNothing().when(permissionRepository).deleteById(permissionName);

    // when
    permissionService.deletePermission(permissionName);

    // then
    verify(permissionRepository, times(1)).deleteById(permissionName);
}


}
