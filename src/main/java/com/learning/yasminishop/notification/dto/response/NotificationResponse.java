package com.learning.yasminishop.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class NotificationResponse {

    String id;
    String title;
    String content;
    String thumbnail;
    String link;
    Boolean isRead;

    LocalDateTime createdDate;
}
