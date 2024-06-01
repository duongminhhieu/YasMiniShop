package com.learning.yasminishop.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductAttributeValue> values;
}
