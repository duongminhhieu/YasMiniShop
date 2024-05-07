package com.learning.springsecurity.permission.mapper;

import com.learning.springsecurity.permission.Permission;
import com.learning.springsecurity.permission.dto.request.PermissionRequest;
import com.learning.springsecurity.permission.dto.response.PermissionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
