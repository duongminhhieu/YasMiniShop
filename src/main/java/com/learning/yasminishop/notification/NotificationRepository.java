package com.learning.yasminishop.notification;

import com.learning.yasminishop.common.entity.Notification;
import com.learning.yasminishop.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String>{

    List<Notification> findAllByUserOrderByCreatedDateDesc(User user);
}
