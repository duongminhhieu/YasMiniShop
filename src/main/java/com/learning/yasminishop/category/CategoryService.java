package com.learning.yasminishop.category;

import com.learning.yasminishop.category.dto.request.CategoryCreation;
import com.learning.yasminishop.category.dto.request.CategoryUpdate;
import com.learning.yasminishop.category.dto.response.CategoryAdminResponse;
import com.learning.yasminishop.category.dto.response.CategoryResponse;
import com.learning.yasminishop.category.mapper.CategoryMapper;
import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.common.entity.Category;
import com.learning.yasminishop.common.entity.Product;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.common.exception.ErrorCode;
import com.learning.yasminishop.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final ProductRepository productRepository;


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryAdminResponse getCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        return categoryMapper.toCategoryAdminResponse(category);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        for (Product product : category.getProducts()) {
            product.getCategories().remove(category);
            productRepository.save(product);
        }
        categoryRepository.delete(category);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void toggleAvailability(List<String> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);

        for (Category category : categories) {
            category.setIsAvailable(!category.getIsAvailable());
        }
        categoryRepository.saveAll(categories);
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
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


    @PreAuthorize("hasRole('ADMIN')")
    public PaginationResponse<CategoryAdminResponse> getAllCategoriesAdmin(String name, Boolean isAvailable, Pageable pageable) {

        Page<Category> categories =  categoryRepository.findAll(
                Specification.where(CategorySpecifications.hasName(name))
                        .and(CategorySpecifications.hasIsAvailable(isAvailable)),
                pageable
        );

        PaginationResponse<CategoryAdminResponse> paginationResponse = new PaginationResponse<>();
        paginationResponse.setPage(pageable.getPageNumber() + 1);
        paginationResponse.setTotal(categories.getTotalElements());
        paginationResponse.setItemsPerPage(pageable.getPageSize());
        paginationResponse.setData(categories.map(categoryMapper::toCategoryAdminResponse).toList());

        return paginationResponse;
    }
}
