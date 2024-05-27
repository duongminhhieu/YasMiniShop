package com.learning.yasminishop.rating.dto.response;

import com.learning.yasminishop.product.dto.response.ProductAttributeResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRatingResponse {

    String name;
    BigDecimal price;
    String slug;
    Float averageRating;

    Set<ProductAttributeResponse> attributes;
}
