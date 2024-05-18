package com.learning.yasminishop.category.mapper;

import com.learning.yasminishop.category.dto.request.CategoryCreation;
import com.learning.yasminishop.category.dto.request.CategoryUpdate;
import com.learning.yasminishop.category.dto.response.CategoryResponse;
import com.learning.yasminishop.common.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryCreation categoryCreation);

    CategoryResponse toCategoryResponse(Category category);

    void updateCategory(@MappingTarget Category category, CategoryUpdate categoryUpdate);
}
