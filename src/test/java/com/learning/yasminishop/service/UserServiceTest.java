package com.learning.yasminishop.service;

import com.learning.yasminishop.common.entity.Role;
import com.learning.yasminishop.common.entity.User;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.role.RoleRepository;
import com.learning.yasminishop.role.dto.response.RoleResponse;
import com.learning.yasminishop.user.UserRepository;
import com.learning.yasminishop.user.UserService;
import com.learning.yasminishop.user.dto.request.UserUpdateRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
@TestPropertySource("/test.properties")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Resource
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    private User user;
    private UserUpdateRequest userUpdateRequest;
    private List<User> userList;


    @BeforeEach
    void setUp() {

        roleRepository.save(Role.builder()
                .name("USER")
                .description("User role")
                .build());
        roleRepository.save(Role.builder()
                .name("ADMIN")
                .description("Admin role")
                .build());

        user = User.builder()
                .id("abc-123")
                .email("duongminhhieu@gmail.com")
                .firstName("Hieu")
                .lastName("Duong")
                .dob(LocalDate.of(1999, 1, 1))
                .build();

        userUpdateRequest = UserUpdateRequest.builder()
                .firstName("Hieu")
                .lastName("Duong")
                .password("123456")
                .roles(Set.of("USER", "ADMIN"))
                .build();

        userList = Arrays.asList(
                User.builder()
                        .id("abc-123")
                        .email("user1@gmail.com")
                        .firstName("User")
                        .lastName("One")
                        .build(),
                User.builder()
                        .id("def-456")
                        .email("user2@gmail.com")
                        .firstName("User")
                        .lastName("Two")
                        .build()
        );
    }

    @Test
    @WithMockUser(username = "duongminhhieu@gmail.com")
    void getMyInfo_validRequest_success() {
        // Given

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));

        // When
        var userResponse = userService.getMyInfo();

        // Then
        assertThat(userResponse)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", "abc-123")
                .hasFieldOrPropertyWithValue("email", "duongminhhieu@gmail.com")
                .hasFieldOrPropertyWithValue("firstName", "Hieu")
                .hasFieldOrPropertyWithValue("lastName", "Duong")
                .hasFieldOrPropertyWithValue("dob", LocalDate.of(1999, 1, 1));
    }

    @Test
    @WithMockUser(username = "abc@gmail.com")
    void getMyInfo_userNotFound_error() {
        // Given
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        // When
        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());

        // Then
        assertThat(exception.getErrorCode().getInternalCode())
                .isEqualTo(1006);
        assertThat(exception.getErrorCode().getMessage())
                .isEqualTo("User not found");
    }

    @Test
    @WithMockUser(username = "admin@spring.com", roles = {"ADMIN"})
    void getAllUsers_validRequest_success() {
        // Given
        when(userRepository.findAll())
                .thenReturn(userList);

        // When
        var userResponses = userService.getAllUsers();

        // Then
        assertThat(userResponses)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

    }

    @Test
    @WithMockUser(username = "admin@spring.com", roles = {"ADMIN"})
    void updateUser_validRequest_success() {
        // Given
        when(userRepository.findById(anyString()))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user))
                .thenReturn(user);

        // When
        var userResponse = userService.updateUser("abc-123", userUpdateRequest);

        // Then
        assertThat(userResponse)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", "abc-123")
                .hasFieldOrPropertyWithValue("firstName", "Hieu")
                .hasFieldOrPropertyWithValue("lastName", "Duong")

        ;
        assertThat(userResponse.getRoles())
                .containsExactlyInAnyOrder(
                        RoleResponse.builder()
                                .name("USER")
                                .description("User role")
                                .permissions(Set.of())
                                .build(),
                        RoleResponse.builder()
                                .name("ADMIN")
                                .description("Admin role")
                                .permissions(Set.of())
                                .build()
                );

    }

    @Test
    @WithMockUser(username = "admin@spring.com", roles = {"ADMIN"})
    void updateUser_notFoundUser_error() {
        // Given
        when(userRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        // When
       var exception = assertThrows(AppException.class, () -> userService.updateUser("abc-123", userUpdateRequest));

        // Then
        assertThat(exception.getErrorCode().getInternalCode())
                .isEqualTo(1006);

        assertThat(exception.getErrorCode().getMessage())
                .isEqualTo("User not found");
    }

    @Test
    @WithMockUser(username = "admin@spring.com", roles = {"ADMIN"})
    void deleteUser_valid_success() {
        // Given
       when(userRepository.existsById(anyString()))
                    .thenReturn(true);
        // When
        userService.deleteUser("abc-123");
        // Then
       verify(userRepository, times(1)).deleteById("abc-123");
    }

    @Test
    @WithMockUser(username = "admin@spring.com", roles = {"ADMIN"})
    void getUserById_valid_success() {
        // Given
        when(userRepository.findById(anyString()))
                .thenReturn(Optional.of(user));
        // When
        var response = userService.getUserById("abc-123");
        // Then
        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", "abc-123")
                .hasFieldOrPropertyWithValue("email", "duongminhhieu@gmail.com")
                .hasFieldOrPropertyWithValue("firstName", "Hieu")
                .hasFieldOrPropertyWithValue("lastName", "Duong")
                .hasFieldOrPropertyWithValue("dob", LocalDate.of(1999, 1, 1));

    }

}
