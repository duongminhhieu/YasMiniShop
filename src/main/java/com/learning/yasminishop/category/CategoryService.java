package com.learning.yasminishop.category;

import com.learning.yasminishop.category.dto.request.CategoryCreation;
import com.learning.yasminishop.category.dto.request.CategoryUpdate;
import com.learning.yasminishop.category.dto.response.CategoryAdminResponse;
import com.learning.yasminishop.category.dto.response.CategoryResponse;
import com.learning.yasminishop.category.mapper.CategoryMapper;
import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.common.entity.Category;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
        category.setIsAvailable(true);
        category = categoryRepository.save(category);

        return categoryMapper.toCategoryResponse(category);
    }

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAllByIsAvailable(true);

        return categories.stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public CategoryResponse getBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return categoryMapper.toCategoryResponse(category);
    }

    public CategoryAdminResponse getCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        return categoryMapper.toCategoryAdminResponse(category);
    }

    @Transactional
    public void delete(List<String> ids) {

        List<Category> categories = categoryRepository.findAllById(ids);
        if (categories.size() != ids.size()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        // check if the categories are not used in any product
        for (Category category : categories) {
            if (!category.getProducts().isEmpty()) {
                throw new AppException(ErrorCode.CATEGORY_USED_IN_PRODUCT);
            }
        }
        categoryRepository.deleteAll(categories);
    }

    @Transactional
    public void toggleAvailability(List<String> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);

        if (categories.size() != ids.size()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        for (Category category : categories) {
            if (!category.getProducts().isEmpty()) {
                throw new AppException(ErrorCode.CATEGORY_USED_IN_PRODUCT);
            }
        }

        for (Category category : categories) {
            category.setIsAvailable(!category.getIsAvailable());
        }
        categoryRepository.saveAll(categories);
    }


    @Transactional
    public CategoryResponse update(String id, CategoryUpdate categoryUpdate) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        if (!category.getSlug().equals(categoryUpdate.getSlug()) && categoryRepository.existsBySlug(categoryUpdate.getSlug())) {
            throw new AppException(ErrorCode.SLUG_ALREADY_EXISTS);
        }

        categoryMapper.updateCategory(category, categoryUpdate);
        category = categoryRepository.save(category);

        return categoryMapper.toCategoryResponse(category);
    }


    public PaginationResponse<CategoryAdminResponse> getAllCategoriesAdmin(String name, Boolean isAvailable, Pageable pageable) {

        Page<Category> categories = categoryRepository.findAll(
                Specification.where(CategorySpecifications.hasName(name))
                        .and(CategorySpecifications.hasIsAvailable(isAvailable)),
                pageable
        );

        return PaginationResponse.<CategoryAdminResponse>builder()
                .page(pageable.getPageNumber() + 1)
                .total(categories.getTotalElements())
                .itemsPerPage(pageable.getPageSize())
                .data(categories.map(categoryMapper::toCategoryAdminResponse).toList())
                .build();
    }
}
