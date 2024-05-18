package com.learning.yasminishop.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_order_address")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String contactName;

    private String phone;

    @Column(columnDefinition = "TEXT")
    private String addressLine1;

    @Column(columnDefinition = "TEXT")
    private String addressLine2;

    @Column(columnDefinition = "TEXT")
    private String city;

    private String zipCode;

    private Long districtId;

    @Column(columnDefinition = "TEXT")
    private String districtName;

    private Long stateOrProvinceId;

    @Column(columnDefinition = "TEXT")
    private String stateOrProvinceName;

    private Long countryId;

    @Column(columnDefinition = "TEXT")
    private String countryName;
}
