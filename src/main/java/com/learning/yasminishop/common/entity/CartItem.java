package com.learning.yasminishop.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "t_cart_item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Integer quantity;

    private BigDecimal price;

    @ManyToOne
    private Product product;

    @ManyToOne
    private User user;
}
