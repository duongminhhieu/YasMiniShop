package com.learning.yasminishop.user;

import com.learning.yasminishop.common.dto.APIResponse;
import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.user.dto.request.UserUpdateRequest;
import com.learning.yasminishop.user.dto.response.UserAdminResponse;
import com.learning.yasminishop.user.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
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
    public APIResponse<PaginationResponse<UserAdminResponse>> getAllUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer itemsPerPage
    ){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("User: {} is trying to access all users", authentication.getName());
        authentication.getAuthorities().forEach(authority -> log.info("User has authority: {}", authority.getAuthority()));

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage); // (0, 10) for page 1
        PaginationResponse<UserAdminResponse> userResponses = userService.getAllUsers(pageable);

        return APIResponse.<PaginationResponse<UserAdminResponse>>builder()
                .result(userResponses)
                .build();
    }

    @PatchMapping("/{userId}/toggle-active")
    public APIResponse<UserAdminResponse> toggleActive(@PathVariable String userId){
        UserAdminResponse userResponse = userService.toggleActive(userId);

        return APIResponse.<UserAdminResponse>builder()
                .result(userResponse)
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
