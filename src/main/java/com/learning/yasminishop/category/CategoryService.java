package com.learning.yasminishop.category;

import com.learning.yasminishop.category.dto.request.CategoryCreation;
import com.learning.yasminishop.category.dto.response.CategoryResponse;
import com.learning.yasminishop.category.mapper.CategoryMapper;
import com.learning.yasminishop.common.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    @Transactional
    public CategoryResponse create(CategoryCreation categoryCreation) {

        Category category = categoryMapper.toCategory(categoryCreation);
        category = categoryRepository.save(category);

        return categoryMapper.toCategoryResponse(category);
    }



}
