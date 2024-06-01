package com.learning.yasminishop.product;

import com.learning.yasminishop.category.CategoryRepository;

import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.common.entity.Category;
import com.learning.yasminishop.common.entity.Product;
import com.learning.yasminishop.common.entity.Storage;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.common.exception.ErrorCode;
import com.learning.yasminishop.product.dto.filter.ProductFilter;
import com.learning.yasminishop.product.dto.request.ProductRequest;
import com.learning.yasminishop.product.dto.response.ProductAdminResponse;
import com.learning.yasminishop.product.dto.response.ProductResponse;
import com.learning.yasminishop.product.mapper.ProductMapper;
import com.learning.yasminishop.storage.StorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    private final StorageRepository storageRepository;


    private final ProductMapper productMapper;

    public PaginationResponse<ProductResponse> getAllProducts(
            ProductFilter productFilter,
            Pageable pageable)
    {

        // check if the categoryIds are valid
        List<Category> categories = categoryRepository.findAllById(List.of(productFilter.getCategoryIds()));
        if (categories.size() != productFilter.getCategoryIds().length) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        Page<Product> products = productRepository.findAll(
                Specification.where(ProductSpecifications.hasName(productFilter.getName()))
                        .and(ProductSpecifications.hasIsAvailable(productFilter.getIsAvailable()))
                        .and(ProductSpecifications.hasIsFeatured(productFilter.getIsFeatured()))
                        .and(ProductSpecifications.hasCategory(categories))
                        .and(ProductSpecifications.hasPrice(productFilter.getMinPrice(), productFilter.getMaxPrice()))
                        .and(ProductSpecifications.hasAverageRating(productFilter.getMinRating()))
                , pageable);


        return PaginationResponse.<ProductResponse>builder()
                .page(pageable.getPageNumber() + 1)
                .total(products.getTotalElements())
                .itemsPerPage(pageable.getPageSize())
                .data(products.map(productMapper::toProductResponse).toList())
                .build();
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ProductAdminResponse create(ProductRequest productCreation) {

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

        Set<String> imageIds = productCreation.getImageIds();
        List<Storage> images = storageRepository.findAllById(imageIds);
        if (images.size() != imageIds.size()) {
            throw new AppException(ErrorCode.IMAGE_NOT_FOUND);
        }

        Product product = productMapper.toProduct(productCreation);
        product.setCategories(new HashSet<>(categories));
        product.setImages(new HashSet<>(images));
        product.setIsAvailable(true);
        product.setThumbnail(images.getFirst().getUrl());


        return productMapper.toProductAdminResponse(productRepository.save(product));
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
    public PaginationResponse<ProductAdminResponse> getAllProductsForAdmin(
            ProductFilter productFilter,
            Pageable pageable)
    {

        // check if the categoryIds are valid
        List<Category> categories = categoryRepository.findAllById(List.of(productFilter.getCategoryIds()));
        if (categories.size() != productFilter.getCategoryIds().length) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        Page<Product> products = productRepository.findAll(
                Specification.where(ProductSpecifications.hasName(productFilter.getName()))
                        .and(ProductSpecifications.hasIsAvailable(productFilter.getIsAvailable()))
                        .and(ProductSpecifications.hasIsFeatured(productFilter.getIsFeatured()))
                        .and(ProductSpecifications.hasCategory(categories))
                , pageable);


        return PaginationResponse.<ProductAdminResponse>builder()
                .page(pageable.getPageNumber() + 1)
                .total(products.getTotalElements())
                .itemsPerPage(pageable.getPageSize())
                .data(products.map(productMapper::toProductAdminResponse).toList())
                .build();
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void toggleAvailability(List<String> ids) {
        List<Product> products = productRepository.findAllById(ids);

        if (products.size() != ids.size()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        for (Product product : products) {
            product.setIsAvailable(!product.getIsAvailable());
        }

        productRepository.saveAll(products);
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ProductAdminResponse update(String id, ProductRequest productUpdate) {

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

        Set<String> imageIds = productUpdate.getImageIds();
        List<Storage> images = storageRepository.findAllById(imageIds);
        if (images.size() != imageIds.size()) {
            throw new AppException(ErrorCode.IMAGE_NOT_FOUND);
        }

        productMapper.updateProduct(product, productUpdate);
        product.setCategories(new HashSet<>(categories));
        product.getImages().clear();
        product.getImages().addAll(images); // update images
        product.setThumbnail(images.getFirst().getUrl());

        return productMapper.toProductAdminResponse(productRepository.save(product));
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(List<String> ids) {

        List<Product> products = productRepository.findAllById(ids);
        if (products.size() != ids.size()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        productRepository.deleteAll(products);
    }

}
