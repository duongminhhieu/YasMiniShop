package com.learning.yasminishop.cart.dto.response;

import com.learning.yasminishop.product.dto.response.ProductAttributeResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCartResponse {

    String id;
    String name;
    String description;
    BigDecimal price;
    String slug;
    String sku;
    Boolean isFeatured;
    Long quantity;

    Float averageRating;
    Boolean isAvailable;
    String thumbnail;

    Set<ProductAttributeResponse> attributes;
}
