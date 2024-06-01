package com.learning.yasminishop.product;

import com.learning.yasminishop.common.entity.Category;
import com.learning.yasminishop.common.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
public class ProductSpecifications {
    public static Specification<Product> hasName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        String lowerCaseName = name.trim().toLowerCase();
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + lowerCaseName + "%");
    }

    public static Specification<Product> hasIsAvailable(Boolean isAvailable) {
        if (isAvailable == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isAvailable"), isAvailable);
    }

    public static Specification<Product> hasCategory(List<Category> categories) {

        return (root, query, criteriaBuilder) -> {
            if (categories == null || categories.isEmpty()) {
                return null;
            }
            return root.join("categories").in(categories);
        };
    }

    public static  Specification<Product> hasIsFeatured(Boolean isFeatured) {
        if (isFeatured == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isFeatured"), isFeatured);
    }

    public static Specification<Product> hasPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        String price = "price";
        if (minPrice == null && maxPrice == null) {
            return null;
        }
        if (minPrice == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(price), maxPrice);
        }
        if (maxPrice == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(price), minPrice);
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(price), minPrice, maxPrice);
    }

    public static Specification<Product> hasAverageRating(Float minRating) {

        if (minRating == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("averageRating"), minRating);
    }


    private ProductSpecifications() {
    }

}
