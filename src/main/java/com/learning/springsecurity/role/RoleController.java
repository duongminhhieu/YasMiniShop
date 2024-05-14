package com.learning.springsecurity.role;

import com.learning.springsecurity.auth.dto.response.APIResponse;
import com.learning.springsecurity.role.dto.request.RoleRequest;
import com.learning.springsecurity.role.dto.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    APIResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return APIResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    APIResponse<List<RoleResponse>> getAll() {
        return APIResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    APIResponse<String> delete(@PathVariable String role) {
        roleService.delete(role);
        return APIResponse.<String>builder()
                .result("Role deleted successfully")
                .build();
    }

}
