package com.learning.yasminishop.user.mapper;

import com.learning.yasminishop.auth.dto.request.RegisterRequest;
import com.learning.yasminishop.common.entity.User;
import com.learning.yasminishop.user.dto.request.UserUpdateRequest;
import com.learning.yasminishop.user.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequest registerRequest);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}
