package com.learning.yasminishop.service;

import com.learning.yasminishop.common.configs.security.JwtService;
import com.learning.yasminishop.common.entity.Notification;
import com.learning.yasminishop.common.entity.User;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.notification.NotificationRepository;
import com.learning.yasminishop.notification.NotificationService;
import com.learning.yasminishop.notification.dto.response.NotificationResponse;
import com.learning.yasminishop.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
@TestPropertySource("/test.properties")
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    private User user;
    private Notification notification;
    private NotificationResponse notificationResponse;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("user@test.com")
                .password("password")
                .build();

        notification = Notification.builder()
                .id("notification-1")
                .title("Test notification")
                .content("Test content")
                .isRead(false)
                .user(user)
                .build();

        notificationResponse = NotificationResponse.builder()
                .id("notification-1")
                .title("Test notification")
                .content("Test content")
                .isRead(false)
                .build();
    }

    @Nested
    class HappyCase {

        @Test
        @WithMockUser(username = "user@test.com")
        void getNotifications_validUser_success() {
            // GIVEN
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
            when(notificationRepository.findAllByUserOrderByCreatedDateDesc(user))
                    .thenReturn(List.of(notification));

            // WHEN
            List<NotificationResponse> result = notificationService.getNotifications();

            // THEN
            assertThat(result).isNotNull();
            assertThat(result.size()).isEqualTo(1);
            assertThat(result.getFirst().getId()).isEqualTo("notification-1");
            assertThat(result.getFirst().getTitle()).isEqualTo("Test notification");
        }

        @Test
        @WithMockUser(username = "user@test.com")
        void getNotifications_noNotifications_emptyList() {
            // GIVEN
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
            when(notificationRepository.findAllByUserOrderByCreatedDateDesc(user))
                    .thenReturn(List.of());

            // WHEN
            List<NotificationResponse> result = notificationService.getNotifications();

            // THEN
            assertThat(result).isNotNull();
            assertThat(result.size()).isEqualTo(0);
        }

        @Test
        void addEmitter_validToken_success() {
            // GIVEN
            SseEmitter emitter = new SseEmitter();
            String token = "valid-token";
            when(jwtService.isTokenValid(token)).thenReturn(true);
            when(jwtService.extractUserEmail(token)).thenReturn("user@test.com");

            // WHEN
            notificationService.addEmitter(emitter, token);

            // THEN
            verify(jwtService).isTokenValid(token);
            verify(jwtService).extractUserEmail(token);
        }

        @Test
        void addEmitter_invalidToken_completeWithError() {
            // GIVEN
            SseEmitter emitter = mock(SseEmitter.class);
            String token = "invalid-token";
            when(jwtService.isTokenValid(token)).thenReturn(false);

            // WHEN
            notificationService.addEmitter(emitter, token);

            // THEN
            verify(jwtService).isTokenValid(token);
            verify(jwtService, never()).extractUserEmail(anyString());
        }

        @Test
        void createNotification_validNotification_saved() {
            // GIVEN
            when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

            // WHEN
            notificationService.createNotification(notification);

            // THEN
            verify(notificationRepository).save(notification);
        }

    }

    @Nested
    class UnHappyCase {

        @Test
        @WithMockUser(username = "nonexistent@test.com")
        void getNotifications_userNotFound_throwException() {
            // GIVEN
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

            // WHEN
            var exception = assertThrows(AppException.class,
                    () -> notificationService.getNotifications());

            // THEN
            assertThat(exception.getErrorCode().getInternalCode()).isEqualTo(1006);
        }

    }

}
