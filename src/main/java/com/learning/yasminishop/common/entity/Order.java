package com.learning.yasminishop.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private String orderTrackingNumber;

    private int totalQuantity;

    private String status;

    private LocalDateTime orderDate;

    private LocalDate shippingDate;

    private String shippingMethod;

    private String paymentMethod;

    @ManyToOne
    private User user;

    @OneToOne
    private OrderAddress orderAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems;

}
