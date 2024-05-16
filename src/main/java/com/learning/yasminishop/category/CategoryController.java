package com.learning.yasminishop.category;

import com.learning.yasminishop.common.dto.APIResponse;
import com.learning.yasminishop.category.dto.request.CategoryCreation;
import com.learning.yasminishop.category.dto.response.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryCreation categoryCreation){
        CategoryResponse categoryResponse = categoryService.create(categoryCreation);
        return APIResponse.<CategoryResponse>builder()
                .result(categoryResponse)
                .build();
    }

    @GetMapping
    public APIResponse<List<CategoryResponse>> getAllCategories(){
        List<CategoryResponse> categoryResponses = categoryService.getAllCategories();

        return APIResponse.<List<CategoryResponse>>builder()
                .result(categoryResponses)
                .build();
    }

}
