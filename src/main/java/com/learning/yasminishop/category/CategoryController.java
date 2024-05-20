package com.learning.yasminishop.category;

import com.learning.yasminishop.category.dto.request.CategoryUpdate;
import com.learning.yasminishop.category.dto.response.CategoryAdminResponse;
import com.learning.yasminishop.common.dto.APIResponse;
import com.learning.yasminishop.category.dto.request.CategoryCreation;
import com.learning.yasminishop.category.dto.response.CategoryResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @DeleteMapping("/{id}")
    public APIResponse<String> softDeleteCategory(@NotNull @NotEmpty @PathVariable String id){
        categoryService.softDelete(id);
        return APIResponse.<String>builder()
                .result("Category deleted successfully")
                .build();
    }

    @PutMapping("/{id}")
    public APIResponse<CategoryResponse> updateCategory(@NotNull @NotEmpty @PathVariable String id, @Valid @RequestBody CategoryUpdate categoryUpdate){
        CategoryResponse categoryResponse = categoryService.update(id, categoryUpdate);
        return APIResponse.<CategoryResponse>builder()
                .result(categoryResponse)
                .build();
    }

    // get all categories for admin
    @GetMapping("/admin")
    public APIResponse<List<CategoryAdminResponse>> getAllCategoriesForAdmin(){
        List<CategoryAdminResponse> categoryResponses = categoryService.getAllCategoriesAdmin();

        return APIResponse.<List<CategoryAdminResponse>>builder()
                .result(categoryResponses)
                .build();
    }

}
