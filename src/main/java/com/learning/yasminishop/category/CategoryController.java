package com.learning.yasminishop.category;

import com.learning.yasminishop.auth.dto.response.APIResponse;
import com.learning.yasminishop.category.dto.request.CategoryCreation;
import com.learning.yasminishop.category.dto.response.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public APIResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryCreation categoryCreation){
        CategoryResponse categoryResponse = categoryService.create(categoryCreation);
        return APIResponse.<CategoryResponse>builder()
                .result(categoryResponse)
                .build();
    }

}
