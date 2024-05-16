package com.learning.yasminishop.category;

import com.learning.yasminishop.category.dto.request.CategoryCreation;
import com.learning.yasminishop.category.dto.response.CategoryResponse;
import com.learning.yasminishop.category.mapper.CategoryMapper;
import com.learning.yasminishop.common.entity.Category;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    @Transactional
    public CategoryResponse create(CategoryCreation categoryCreation) {

        if (categoryRepository.existsBySlug(categoryCreation.getSlug())) {
            throw new AppException(ErrorCode.SLUG_ALREADY_EXISTS);
        }

        Category category = categoryMapper.toCategory(categoryCreation);
        category = categoryRepository.save(category);

        return categoryMapper.toCategoryResponse(category);
    }

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }


}
