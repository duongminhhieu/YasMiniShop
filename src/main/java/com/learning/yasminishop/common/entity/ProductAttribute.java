package com.learning.yasminishop.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_product_attribute")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String value;

    @ManyToOne
    private Product product;
}
