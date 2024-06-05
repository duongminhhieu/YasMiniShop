package com.learning.yasminishop.notification;

import com.learning.yasminishop.common.dto.APIResponse;
import com.learning.yasminishop.notification.dto.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(path = "/subscribe/{token}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe( @PathVariable String token) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        notificationService.addEmitter(emitter, token);
        return emitter;
    }

    @GetMapping
    public APIResponse<List<NotificationResponse>> getNotifications() {
        List<NotificationResponse> notifications = notificationService.getNotifications();
        return APIResponse.<List<NotificationResponse>>builder()
                .result(notifications)
                .build();
    }

}
