package com.learning.yasminishop.notification;

import com.learning.yasminishop.common.configs.security.JwtService;
import com.learning.yasminishop.common.entity.Notification;
import com.learning.yasminishop.common.entity.User;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.common.exception.ErrorCode;
import com.learning.yasminishop.notification.dto.response.NotificationResponse;
import com.learning.yasminishop.notification.mapper.NotificationMapper;
import com.learning.yasminishop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    private final JwtService jwtService;
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();


    public void addEmitter(SseEmitter emitter, String token) {

        if(!jwtService.isTokenValid(token)) {
            log.error("Token is not valid");
            emitter.completeWithError(new AppException(ErrorCode.INVALID_TOKEN));
            return;
        }

        String email = jwtService.extractUserEmail(token);
        log.info("User with email {} subscribed to notifications", email);
        emitters.put(email, emitter);
    }

    public void removeEmitter(String email) {
        emitters.remove(email);
    }

    public void sendNotification(String clientId, NotificationResponse notificationResponse) {
        SseEmitter emitter = emitters.get(clientId);
        if (emitter != null) {
            try {
                emitter.send(notificationResponse);
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(clientId);
            }
        }
    }

    public List<NotificationResponse> getNotifications() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Notification> notifications = notificationRepository.findAllByUserOrderByCreatedDateDesc(user);
        return notifications
                .stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }

    public void createNotification(Notification notification) {
        notificationRepository.save(notification);
        sendNotification(notification.getUser().getEmail(), notificationMapper.toNotificationResponse(notification));
    }

}
