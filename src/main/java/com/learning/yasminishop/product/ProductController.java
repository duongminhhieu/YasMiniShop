package com.learning.yasminishop.product;

import com.learning.yasminishop.common.dto.APIResponse;
import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.product.dto.payload.FilterProductPayload;
import com.learning.yasminishop.product.dto.request.ProductRequest;
import com.learning.yasminishop.product.dto.response.ProductAdminResponse;
import com.learning.yasminishop.product.dto.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productCreation) {
        log.info("Creating product: {}", productCreation);
        ProductResponse productResponse = productService.create(productCreation);
        return APIResponse.<ProductResponse>builder()
                .result(productResponse)
                .build();
    }

    @GetMapping("/{slug}")
    public APIResponse<ProductResponse> getBySlug(@PathVariable String slug) {
        log.info("Getting product by slug: {}", slug);
        ProductResponse productResponse = productService.getBySlug(slug);
        return APIResponse.<ProductResponse>builder()
                .result(productResponse)
                .build();
    }

    @GetMapping("/id/{id}")
    public APIResponse<ProductAdminResponse> getById(@PathVariable String id) {
        log.info("Getting product by id: {}", id);
        ProductAdminResponse productResponse = productService.getById(id);
        return APIResponse.<ProductAdminResponse>builder()
                .result(productResponse)
                .build();
    }

    @GetMapping
    public APIResponse<PaginationResponse<ProductAdminResponse>> getAllForAdmin(@Valid @RequestBody FilterProductPayload filterProductPayload) {

        var paginationResponse = productService.getAllProductsPaginationForAdmin(filterProductPayload);

        return APIResponse.<PaginationResponse<ProductAdminResponse>>builder()
                .result(paginationResponse)
                .build();
    }

    @GetMapping("/featured")
    public APIResponse<PaginationResponse<ProductResponse>> getFeaturedProducts(@Valid @RequestBody FilterProductPayload filterProductPayload) {
        log.info("Getting featured products with filter: {}", filterProductPayload);
        var paginationResponse = productService.getFeaturedProducts(filterProductPayload);
        return APIResponse.<PaginationResponse<ProductResponse>>builder()
                .result(paginationResponse)
                .build();
    }

    @PutMapping("/{id}")
    public APIResponse<ProductResponse> updateProduct(@PathVariable String id, @Valid @RequestBody ProductRequest productUpdate) {
        log.info("Updating product with id: {}", id);
        ProductResponse productResponse = productService.update(id, productUpdate);
        return APIResponse.<ProductResponse>builder()
                .result(productResponse)
                .build();
    }

    @DeleteMapping("/{id}")
    public APIResponse<String> deleteProduct(@PathVariable String id) {
        log.info("Deleting product with id: {}", id);
        productService.softDelete(id);
        return APIResponse.<String>builder()
                .result("Product deleted successfully")
                .build();
    }

}
