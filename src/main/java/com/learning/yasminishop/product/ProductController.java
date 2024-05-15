package com.learning.yasminishop.product;

import com.learning.yasminishop.auth.dto.response.APIResponse;
import com.learning.yasminishop.product.dto.request.ProductCreation;
import com.learning.yasminishop.product.dto.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;


    @PostMapping("/create")
    public APIResponse<ProductResponse> createProduct(@Valid @RequestBody ProductCreation productCreation){
        log.info("Creating product: {}", productCreation);
        ProductResponse productResponse = productService.create(productCreation);
        return APIResponse.<ProductResponse>builder()
                .result(productResponse)
                .build();
    }

}
