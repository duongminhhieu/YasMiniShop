package com.learning.yasminishop.product;

import com.learning.yasminishop.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>{
    boolean existsBySlug(String slug);

    Optional<Product> findBySlug(String slug);
}
