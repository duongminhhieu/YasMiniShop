package com.learning.yasminishop.product;

import com.learning.yasminishop.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> , JpaSpecificationExecutor<Product> {
    boolean existsBySlug(String slug);

    boolean existsBySku(String sku);

    Optional<Product> findBySlug(String slug);

    List<Product> findTop10ByOrderByQuantityDesc();
}

