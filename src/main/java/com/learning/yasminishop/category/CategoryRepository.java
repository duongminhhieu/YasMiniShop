package com.learning.yasminishop.category;

import com.learning.yasminishop.common.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String>, JpaSpecificationExecutor<Category> {
    boolean existsBySlug(String slug);
    List<Category> findAllByIsAvailable(Boolean isAvailable);
}
