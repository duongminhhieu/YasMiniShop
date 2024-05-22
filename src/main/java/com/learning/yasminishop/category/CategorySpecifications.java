package com.learning.yasminishop.category;

import com.learning.yasminishop.common.entity.Category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public class CategorySpecifications {

    public static Specification<Category> hasName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        String lowerCaseName = name.trim().toLowerCase();
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + lowerCaseName + "%");
    }

    public static Specification<Category> hasIsAvailable(Boolean isAvailable) {
        if (isAvailable == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isAvailable"), isAvailable);
    }

    private CategorySpecifications() {
    }

}
