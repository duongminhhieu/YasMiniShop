package com.learning.yasminishop.rating;

import com.learning.yasminishop.common.entity.Product;
import com.learning.yasminishop.common.entity.Rating;
import com.learning.yasminishop.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, String>{
    boolean existsByProductAndUser(Product product, User user);
    Page<Rating> findByProduct(Product product, Pageable pageable);
}
