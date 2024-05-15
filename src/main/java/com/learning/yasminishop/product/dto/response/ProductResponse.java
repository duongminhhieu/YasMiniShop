package com.learning.yasminishop.product.dto.response;

import com.learning.yasminishop.category.dto.response.CategoryResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

    String id;
    String name;
    String description;
    String slug;
    BigDecimal price;
    Long quantity;
    Set<CategoryResponse> categories;
}
