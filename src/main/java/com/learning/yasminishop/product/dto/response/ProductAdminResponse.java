package com.learning.yasminishop.product.dto.response;

import com.learning.yasminishop.category.dto.response.CategoryResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductAdminResponse {

    String id;
    String name;
    String description;
    String slug;
    String sku;
    BigDecimal price;
    Boolean isFeatured;
    Long quantity;
    Set<CategoryResponse> categories;

    String createdBy;
    LocalDateTime createdDate;
    String lastModifiedBy;
    LocalDateTime lastModifiedDate;
}
