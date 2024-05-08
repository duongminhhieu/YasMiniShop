package com.learning.springsecurity.user;

import com.learning.springsecurity.auth.dto.response.APIResponse;
import com.learning.springsecurity.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping("/me")
    public APIResponse<UserResponse> getMyInfo(){
        UserResponse userResponse = userService.getMyInfo();

        return APIResponse.<UserResponse>builder()
                .result(userResponse)
                .build();
    }
}
