package com.learning.yasminishop.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
        private String id;

        private int rating;
        private String productId;
        private String userId;

}
