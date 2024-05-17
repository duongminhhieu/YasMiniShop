package com.learning.yasminishop.common.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "t_product")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AuditEntity<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal price;

    @Column(unique = true)
    private String slug;

    @Column(unique = true, nullable = false)
    private String sku;

    @ColumnDefault("false")
    private Boolean isFeatured;

    private Long quantity;

    private Float averageRating;

    @ColumnDefault("false")
    private Boolean isAvailable;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductAttribute> attributes;

    @ManyToMany
    private Set<Category> categories;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Storage> images;

}
