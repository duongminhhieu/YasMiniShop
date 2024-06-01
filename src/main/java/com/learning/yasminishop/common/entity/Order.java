package com.learning.yasminishop.common.entity;

import com.learning.yasminishop.common.enumeration.EOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "t_order")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends AuditEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Integer totalQuantity;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private EOrderStatus status;

    @ManyToOne
    private User user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private OrderAddress orderAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems;

}
