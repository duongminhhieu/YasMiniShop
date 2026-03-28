package com.learning.yasminishop.controller;

import com.learning.yasminishop.notification.NotificationService;
import com.learning.yasminishop.notification.dto.response.NotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class NotificationControllerTest {

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private MockMvc mockMvc;

    private NotificationResponse notificationResponse;

    @BeforeEach
    void setUp() {
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
        void getNotifications_validRequest_success() throws Exception {
            // GIVEN
            when(notificationService.getNotifications()).thenReturn(List.of(notificationResponse));

            // WHEN THEN
            mockMvc.perform(MockMvcRequestBuilders.get("/notifications"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("internalCode").value(1000))
                    .andExpect(jsonPath("result").isArray())
                    .andExpect(jsonPath("result[0].id").value("notification-1"))
                    .andExpect(jsonPath("result[0].title").value("Test notification"))
                    .andExpect(jsonPath("result[0].content").value("Test content"))
                    .andExpect(jsonPath("result[0].isRead").value(false));
        }

        @Test
        @WithMockUser(username = "user@test.com")
        void getNotifications_emptyList_success() throws Exception {
            // GIVEN
            when(notificationService.getNotifications()).thenReturn(List.of());

            // WHEN THEN
            mockMvc.perform(MockMvcRequestBuilders.get("/notifications"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("internalCode").value(1000))
                    .andExpect(jsonPath("result").isArray())
                    .andExpect(jsonPath("result").isEmpty());
        }

    }

    @Nested
    class UnHappyCase {

        @Test
        void getNotifications_unauthenticated_failure() throws Exception {
            // WHEN THEN
            mockMvc.perform(MockMvcRequestBuilders.get("/notifications"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("internalCode").value(1005));
        }

    }

}
