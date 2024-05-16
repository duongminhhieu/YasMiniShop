package com.learning.yasminishop.product;

import com.learning.yasminishop.common.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>{
    boolean existsBySlug(String slug);
    boolean existsBySku(String sku);

    Optional<Product> findBySlug(String slug);

    long countByIsFeatured(Boolean isFeatured);

    List<Product> findByIsFeatured(Boolean isFeatured, Pageable pageable);
}
