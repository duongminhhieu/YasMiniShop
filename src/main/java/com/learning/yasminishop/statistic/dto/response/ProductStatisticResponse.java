package com.learning.yasminishop.statistic.dto.response;

import com.learning.yasminishop.storage.dto.response.StorageResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductStatisticResponse {

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

    Set<StorageResponse> images;
}
