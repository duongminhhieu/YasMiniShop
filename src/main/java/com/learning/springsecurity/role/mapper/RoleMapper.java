package com.learning.springsecurity.role.mapper;

import com.learning.springsecurity.common.entity.Role;
import com.learning.springsecurity.role.dto.request.RoleRequest;
import com.learning.springsecurity.role.dto.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
