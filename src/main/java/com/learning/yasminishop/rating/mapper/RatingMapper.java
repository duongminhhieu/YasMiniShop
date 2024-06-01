package com.learning.yasminishop.rating.mapper;

import com.learning.yasminishop.common.entity.Rating;
import com.learning.yasminishop.rating.dto.request.RatingRequest;
import com.learning.yasminishop.rating.dto.response.RatingResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    Rating toRating(RatingRequest ratingRequest);

    RatingResponse toRatingResponse(Rating rating);

}
