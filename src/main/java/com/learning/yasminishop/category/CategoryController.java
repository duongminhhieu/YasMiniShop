package com.learning.yasminishop.category;

import com.learning.yasminishop.category.dto.request.CategoryUpdate;
import com.learning.yasminishop.category.dto.response.CategoryAdminResponse;
import com.learning.yasminishop.common.dto.APIResponse;
import com.learning.yasminishop.category.dto.request.CategoryCreation;
import com.learning.yasminishop.category.dto.response.CategoryResponse;
import com.learning.yasminishop.common.dto.PaginationResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/admin")
    public APIResponse<PaginationResponse<CategoryAdminResponse>> getAllCategoriesForAdmin(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isAvailable,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer itemsPerPage
    ){

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage); // (0, 10) for page 1

        PaginationResponse<CategoryAdminResponse> paginationResponse = categoryService.getAllCategoriesAdmin(name, isAvailable, pageable);

        return APIResponse.<PaginationResponse<CategoryAdminResponse>>builder()
                .result(paginationResponse)
                .build();
    }

}
