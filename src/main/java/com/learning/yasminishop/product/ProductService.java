package com.learning.yasminishop.product;

import com.learning.yasminishop.category.CategoryRepository;
import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.common.entity.Category;
import com.learning.yasminishop.common.entity.Product;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.common.exception.ErrorCode;
import com.learning.yasminishop.product.dto.payload.FilterProductPayload;
import com.learning.yasminishop.product.dto.request.ProductRequest;
import com.learning.yasminishop.product.dto.response.ProductAdminResponse;
import com.learning.yasminishop.product.dto.response.ProductResponse;
import com.learning.yasminishop.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse create(ProductRequest productCreation) {

        if (productRepository.existsBySlug(productCreation.getSlug())) {
            throw new AppException(ErrorCode.SLUG_ALREADY_EXISTS);
        }

        if (productRepository.existsBySku(productCreation.getSku())) {
            throw new AppException(ErrorCode.SKU_ALREADY_EXISTS);
        }

        Set<String> categoryIds = productCreation.getCategoryIds();
        List<Category> categories = categoryRepository.findAllById(categoryIds);

        if (categories.size() != categoryIds.size()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        Product product = productMapper.toProduct(productCreation);
        product.setCategories(new HashSet<>(categories));
        product.setIsAvailable(true);

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public ProductResponse getBySlug(String slug) {

        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        return productMapper.toProductResponse(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProductAdminResponse getById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        return productMapper.toProductAdminResponse(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PaginationResponse<ProductAdminResponse> getAllProductsPaginationForAdmin(FilterProductPayload filterProductPayload) {

        PaginationResponse<ProductAdminResponse> paginationResponse = new PaginationResponse<>();
        paginationResponse.setPage(filterProductPayload.getPage());
        paginationResponse.setTotal(productRepository.countByIsAvailable(true));
        paginationResponse.setItemsPerPage(filterProductPayload.getItemsPerPage());

        Pageable pageable = PageRequest.of(filterProductPayload.getPage() - 1, filterProductPayload.getItemsPerPage());

        var products = productRepository.findProductByIsAvailable(true, pageable);
        List<ProductAdminResponse> productResponses = products.stream()
                .map(productMapper::toProductAdminResponse)
                .toList();

        paginationResponse.setData(productResponses);

        return paginationResponse;
    }

    public PaginationResponse<ProductResponse> getFeaturedProducts(FilterProductPayload filterProductPayload) {

        PaginationResponse<ProductResponse> paginationResponse = new PaginationResponse<>();
        paginationResponse.setPage(filterProductPayload.getPage());
        paginationResponse.setTotal(productRepository.countByIsFeatured(true));
        paginationResponse.setItemsPerPage(filterProductPayload.getItemsPerPage());


        Pageable pageable = PageRequest.of(filterProductPayload.getPage() - 1, filterProductPayload.getItemsPerPage());
        var products = productRepository.findByIsFeatured(true, pageable);

        List<ProductResponse> productResponses = products.stream()
                .map(productMapper::toProductResponse)
                .toList();

        paginationResponse.setData(productResponses);

        return paginationResponse;
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse update(String id, ProductRequest productUpdate) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (!product.getSlug().equals(productUpdate.getSlug()) && categoryRepository.existsBySlug(productUpdate.getSlug())) {
            throw new AppException(ErrorCode.SLUG_ALREADY_EXISTS);
        }

        if (!product.getSku().equals(productUpdate.getSku()) && categoryRepository.existsBySlug(productUpdate.getSku())) {
            throw new AppException(ErrorCode.SKU_ALREADY_EXISTS);
        }

        Set<String> categoryIds = productUpdate.getCategoryIds();
        List<Category> categories = categoryRepository.findAllById(categoryIds);

        if (categories.size() != categoryIds.size()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        productMapper.updateProduct(product, productUpdate);
        product.setCategories(new HashSet<>(categories));

        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void softDelete(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.setIsAvailable(false);

        productRepository.save(product);
    }

}
