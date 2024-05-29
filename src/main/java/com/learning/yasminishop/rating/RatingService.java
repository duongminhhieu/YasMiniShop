package com.learning.yasminishop.rating;

import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.common.entity.Product;
import com.learning.yasminishop.common.entity.Rating;
import com.learning.yasminishop.common.entity.User;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.common.exception.ErrorCode;
import com.learning.yasminishop.product.ProductRepository;
import com.learning.yasminishop.rating.dto.request.RatingRequest;
import com.learning.yasminishop.rating.dto.response.RatingResponse;
import com.learning.yasminishop.rating.mapper.RatingMapper;
import com.learning.yasminishop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RatingService {

    private final RatingRepository ratingRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final RatingMapper ratingMapper;


    @Transactional
    @PreAuthorize("hasRole('USER')")
    public RatingResponse create(RatingRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // check if the user exists
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // check if the user has already rated the product
        if (ratingRepository.existsByProductAndUser(product, user)) {
            throw new AppException(ErrorCode.RATING_ALREADY_EXISTS);
        }

        // create the rating
        Rating rating = ratingMapper.toRating(request);
        rating.setProduct(product);
        rating.setUser(user);
        rating = ratingRepository.save(rating);

        // calculate the average rating
        calculateAverageRating(product);

        return ratingMapper.toRatingResponse(rating);
    }

    public void calculateAverageRating(Product product) {
        List<Rating> ratings = ratingRepository.findByProductId(product.getId());

        double averageRating = ratings.stream()
                .mapToInt(Rating::getStar)
                .average()
                .orElse(0.0);

        product.setAverageRating((float) averageRating);
        productRepository.save(product);
    }


    public PaginationResponse<RatingResponse> getRatings(String productId, Pageable pageable) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Page<Rating> ratingResponseList = ratingRepository.findByProduct(product, pageable);

        return PaginationResponse.<RatingResponse>builder()
                .page(pageable.getPageNumber() + 1)
                .total(ratingResponseList.getTotalElements())
                .itemsPerPage(pageable.getPageSize())
                .data(ratingResponseList.map(ratingMapper::toRatingResponse).toList())
                .build();
    }

}
