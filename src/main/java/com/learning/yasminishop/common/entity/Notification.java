package com.learning.yasminishop.common.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "t_notification")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification extends  AuditEntity<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(columnDefinition = "TEXT")
    String title;

    @Column(columnDefinition = "TEXT")
    String content;

    String thumbnail;

    String link;

    Boolean isRead = false;

    @ManyToOne
    User user;

}
