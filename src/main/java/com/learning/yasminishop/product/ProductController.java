package com.learning.yasminishop.product;

import com.learning.yasminishop.common.dto.APIResponse;
import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.common.utility.PageSortUtility;
import com.learning.yasminishop.product.dto.filter.ProductFilter;
import com.learning.yasminishop.product.dto.request.ProductIds;
import com.learning.yasminishop.product.dto.request.ProductRequest;
import com.learning.yasminishop.product.dto.response.ProductAdminResponse;
import com.learning.yasminishop.product.dto.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final PageSortUtility pageSortUtility;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<ProductAdminResponse> createProduct(@Valid @RequestBody ProductRequest productCreation) {
        ProductAdminResponse productResponse = productService.create(productCreation);
        return APIResponse.<ProductAdminResponse>builder()
                .result(productResponse)
                .build();
    }

    @GetMapping("/{slug}")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<ProductResponse> getBySlug(@PathVariable String slug) {
        log.info("Getting product by slug: {}", slug);
        ProductResponse productResponse = productService.getBySlug(slug);
        return APIResponse.<ProductResponse>builder()
                .result(productResponse)
                .build();
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<ProductAdminResponse> getById(@PathVariable String id) {
        log.info("Getting product by id: {}", id);
        ProductAdminResponse productResponse = productService.getById(id);
        return APIResponse.<ProductAdminResponse>builder()
                .result(productResponse)
                .build();
    }

    @GetMapping("/admin")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<PaginationResponse<ProductAdminResponse>> getAllForAdmin(
            @Valid @ModelAttribute ProductFilter productFilter) {

        Pageable pageable = pageSortUtility.createPageable(productFilter.getPage(),
                productFilter.getItemsPerPage(),
                productFilter.getSortBy(),
                productFilter.getOrderBy());
        PaginationResponse<ProductAdminResponse> products = productService.getAllProductsForAdmin(productFilter, pageable);

        return APIResponse.<PaginationResponse<ProductAdminResponse>>builder()
                .result(products)
                .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<PaginationResponse<ProductResponse>> getAll(
            @Valid @ModelAttribute ProductFilter productFilter
    ) {

        Pageable pageable = pageSortUtility.createPageable(productFilter.getPage(),
                productFilter.getItemsPerPage(),
                productFilter.getSortBy(),
                productFilter.getOrderBy());

        PaginationResponse<ProductResponse> products = productService.getAllProducts(productFilter, pageable);

        return APIResponse.<PaginationResponse<ProductResponse>>builder()
                .result(products)
                .build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<ProductAdminResponse> updateProduct(@PathVariable String id, @Valid @RequestBody ProductRequest productUpdate) {
        log.info("Updating product with id: {}", id);
        ProductAdminResponse productResponse = productService.update(id, productUpdate);
        return APIResponse.<ProductAdminResponse>builder()
                .result(productResponse)
                .build();
    }

    @PatchMapping("/toggle-availability")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<String> toggleAvailability(@RequestBody ProductIds productIds) {
        productService.toggleAvailability(productIds.getIds());

        return APIResponse.<String>builder()
                .message("Products availability toggled successfully")
                .build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<String> deleteProducts(@RequestBody ProductIds productIds) {
        productService.delete(productIds.getIds());
        return APIResponse.<String>builder()
                .message("Category deleted successfully")
                .build();
    }

}
