package com.learning.yasminishop.common.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_product_attribute_value")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String value;
}
