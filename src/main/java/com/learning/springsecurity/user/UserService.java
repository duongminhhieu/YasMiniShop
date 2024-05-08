package com.learning.springsecurity.user;

import com.learning.springsecurity.common.exception.AppException;
import com.learning.springsecurity.common.exception.ErrorCode;
import com.learning.springsecurity.user.dto.response.UserResponse;
import com.learning.springsecurity.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

}
