package com.learning.yasminishop.role.mapper;

import com.learning.yasminishop.common.entity.Role;
import com.learning.yasminishop.role.dto.request.RoleRequest;
import com.learning.yasminishop.role.dto.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
