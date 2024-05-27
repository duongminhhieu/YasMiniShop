package com.learning.yasminishop.rating;

import com.learning.yasminishop.common.dto.APIResponse;
import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.rating.dto.request.RatingRequest;
import com.learning.yasminishop.rating.dto.response.RatingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rating")
@RequiredArgsConstructor
@Slf4j
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<RatingResponse> createRating(@Valid @RequestBody RatingRequest ratingRequest) {
        RatingResponse ratingResponse = ratingService.create(ratingRequest);
        return APIResponse.<RatingResponse>builder()
                .result(ratingResponse)
                .build();
    }

    @GetMapping
    public APIResponse<PaginationResponse<RatingResponse>> getRatings(
            @RequestParam String productId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer itemsPerPage
    ) {
        Pageable pageable = PageRequest.of(page - 1, itemsPerPage);

        var ratings = ratingService.getRatings(productId, pageable);

        return APIResponse.<PaginationResponse<RatingResponse>>builder()
                .result(ratings)
                .build();
    }

}
