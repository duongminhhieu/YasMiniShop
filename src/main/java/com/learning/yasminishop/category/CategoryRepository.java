package com.learning.yasminishop.category;

import com.learning.yasminishop.common.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String>{
    boolean existsBySlug(String slug);
}
