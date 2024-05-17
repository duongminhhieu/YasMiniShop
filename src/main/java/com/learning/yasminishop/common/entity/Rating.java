package com.learning.yasminishop.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_rating")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating extends AuditEntity<String> {

        @Id
        @GeneratedValue
        private String id;

        @Column(nullable = false)
        private Integer star;

        @Column(columnDefinition = "TEXT")
        private String comment;

        @ManyToOne
        private Product product;

        @ManyToOne
        private User user;
}
