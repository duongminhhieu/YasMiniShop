package com.learning.springsecurity.user;

import com.learning.springsecurity.auth.dto.response.APIResponse;
import com.learning.springsecurity.user.dto.request.UserUpdateRequest;
import com.learning.springsecurity.user.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public APIResponse<UserResponse> getMyInfo(){
        UserResponse userResponse = userService.getMyInfo();

        return APIResponse.<UserResponse>builder()
                .result(userResponse)
                .build();
    }

    @GetMapping
    public APIResponse<List<UserResponse>> getAllUsers(){

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("User: {} is trying to access all users", authentication.getName());
        authentication.getAuthorities().forEach(authority -> log.info("User has authority: {}", authority.getAuthority()));

        List<UserResponse> userResponses = userService.getAllUsers();

        return APIResponse.<List<UserResponse>>builder()
                .result(userResponses)
                .build();
    }

    @DeleteMapping("{userId}")
    public APIResponse<String> deleteUser(@PathVariable String userId){

        userService.deleteUser(userId);

        return APIResponse.<String>builder()
                .result("User deleted successfully")
                .build();
    }

    @PutMapping("{userId}")
    public APIResponse<UserResponse> updateUser(@PathVariable String userId, @Valid @RequestBody UserUpdateRequest userUpdateRequest){
        UserResponse userResponse = userService.updateUser(userId, userUpdateRequest);

        return APIResponse.<UserResponse>builder()
                .result(userResponse)
                .build();
    }

    @GetMapping("{userId}")
    public APIResponse<UserResponse> getUser(@PathVariable String userId){
        UserResponse userResponse = userService.getUserById(userId);

        return APIResponse.<UserResponse>builder()
                .result(userResponse)
                .build();
    }



}
