package com.learning.yasminishop.permission.mapper;

import com.learning.yasminishop.common.entity.Permission;
import com.learning.yasminishop.permission.dto.request.PermissionRequest;
import com.learning.yasminishop.permission.dto.response.PermissionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
