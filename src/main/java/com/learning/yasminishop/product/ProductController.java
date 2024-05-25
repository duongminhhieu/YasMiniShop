package com.learning.yasminishop.product;

import com.learning.yasminishop.common.dto.APIResponse;
import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.common.exception.ErrorCode;
import com.learning.yasminishop.product.dto.request.ProductIds;
import com.learning.yasminishop.product.dto.request.ProductRequest;
import com.learning.yasminishop.product.dto.response.ProductAdminResponse;
import com.learning.yasminishop.product.dto.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<ProductAdminResponse> createProduct(@Valid @RequestBody ProductRequest productCreation) {
        ProductAdminResponse productResponse = productService.create(productCreation);
        return APIResponse.<ProductAdminResponse>builder()
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

    @GetMapping("/admin")
    public APIResponse<PaginationResponse<ProductAdminResponse>> getAllForAdmin(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isAvailable,
            @RequestParam(required = false) Boolean isFeatured,
            @RequestParam(required = false, defaultValue = "") String[] categoryIds,
            @RequestParam(defaultValue = "price") String[] orderBy,
            @RequestParam(defaultValue = "asc") String sortBy,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer itemsPerPage
    ) {


        List<Sort.Order> orders = new ArrayList<>();

        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(sortBy);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_SORT_DIRECTION);
        }
        if (orderBy != null) {
            for (String order : orderBy) {
                orders.add(new Sort.Order(direction, order));
            }
        }

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage, Sort.by(orders)); // (0, 10) for page 1

        PaginationResponse<ProductAdminResponse> products = productService.getAllProductsForAdmin(name, isAvailable, isFeatured, categoryIds, pageable);

        return APIResponse.<PaginationResponse<ProductAdminResponse>>builder()
                .result(products)
                .build();
    }


    @PutMapping("/{id}")
    public APIResponse<ProductAdminResponse> updateProduct(@PathVariable String id, @Valid @RequestBody ProductRequest productUpdate) {
        log.info("Updating product with id: {}", id);
        ProductAdminResponse productResponse = productService.update(id, productUpdate);
        return APIResponse.<ProductAdminResponse>builder()
                .result(productResponse)
                .build();
    }

    @PatchMapping("/toggle-availability")
    public APIResponse<String> toggleAvailability(@RequestBody ProductIds productIds) {
        productService.toggleAvailability(productIds.getIds());

        return APIResponse.<String>builder()
                .message("Products availability toggled successfully")
                .build();
    }

    @DeleteMapping
    public APIResponse<String> deleteProducts(@RequestBody ProductIds productIds) {
        productService.delete(productIds.getIds());
        return APIResponse.<String>builder()
                .message("Category deleted successfully")
                .build();
    }

}
