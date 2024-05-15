package com.learning.yasminishop.permission;

import com.learning.yasminishop.common.dto.APIResponse;
import com.learning.yasminishop.permission.dto.request.PermissionRequest;
import com.learning.yasminishop.permission.dto.response.PermissionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@Slf4j
public class PermissionController {

    private final PermissionService permissionService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<PermissionResponse> create(@Valid @RequestBody PermissionRequest request) {
        PermissionResponse permissionResponse = permissionService.createPermission(request);

        return APIResponse.<PermissionResponse>builder()
                .result(permissionResponse)
                .build();
    }

    @GetMapping
    public APIResponse<List<PermissionResponse>> getAll() {
        List<PermissionResponse> permissionResponses = permissionService.getALlPermissions();

        return APIResponse.<List<PermissionResponse>>builder()
                .result(permissionResponses)
                .build();
    }

    @DeleteMapping("/{permission}")
    public APIResponse<Void> delete(@PathVariable String permission) {
        permissionService.deletePermission(permission);

        return APIResponse.<Void>builder()
                .build();
    }


}
