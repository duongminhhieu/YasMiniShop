package com.learning.springsecurity.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "t_invalid_token")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvalidToken extends AuditEntity<String> {
    @Id
    private String idToken;
    private Date expiryDate;
}
