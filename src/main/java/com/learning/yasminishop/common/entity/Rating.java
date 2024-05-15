package com.learning.yasminishop.common.entity;

import jakarta.persistence.Entity;
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

        private int rating;
        private String productId;
        private String userId;

}
