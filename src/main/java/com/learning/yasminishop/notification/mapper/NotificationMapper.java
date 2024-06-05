package com.learning.yasminishop.notification.mapper;

import com.learning.yasminishop.common.entity.Notification;
import com.learning.yasminishop.notification.dto.response.NotificationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toNotificationResponse(Notification notification);
}
