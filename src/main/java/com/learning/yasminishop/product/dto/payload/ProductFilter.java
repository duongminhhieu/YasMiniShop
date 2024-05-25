package com.learning.yasminishop.product.dto.payload;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductFilter {
    private String name;
    private Boolean isAvailable;
    private Boolean isFeatured;
    private String[] categoryIds;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Float minRating;
    private String[] orderBy = {"price"};
    private String sortBy = "asc";
    private Integer page = 1;
    private Integer itemsPerPage = 10;
}
