package com.learning.springsecurity.user.mapper;

import com.learning.springsecurity.auth.dto.request.RegisterRequest;
import com.learning.springsecurity.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequest registerRequest);
}
