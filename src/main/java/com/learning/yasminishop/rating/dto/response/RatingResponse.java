package com.learning.yasminishop.rating.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingResponse {

    String id;
    Integer star;
    String comment;
    ProductRatingResponse product;
    UserRatingResponse user;

    String createdDate;
}
