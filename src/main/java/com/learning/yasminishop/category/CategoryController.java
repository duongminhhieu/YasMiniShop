package com.learning.yasminishop.category;

import com.learning.yasminishop.category.dto.request.CategoryIds;
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
    public APIResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryCreation categoryCreation) {
        CategoryResponse categoryResponse = categoryService.create(categoryCreation);
        return APIResponse.<CategoryResponse>builder()
                .result(categoryResponse)
                .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categoryResponses = categoryService.getAllCategories();

        return APIResponse.<List<CategoryResponse>>builder()
                .result(categoryResponses)
                .build();
    }

    @GetMapping("/slug/{slug}")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<CategoryResponse> getCategoryBySlug(@PathVariable String slug) {
        CategoryResponse categoryResponse = categoryService.getBySlug(slug);
        return APIResponse.<CategoryResponse>builder()
                .result(categoryResponse)
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<CategoryAdminResponse> getCategory(@PathVariable String id) {
        CategoryAdminResponse categoryAdminResponse = categoryService.getCategory(id);
        return APIResponse.<CategoryAdminResponse>builder()
                .result(categoryAdminResponse)
                .build();
    }

    @PatchMapping("/toggle-availability")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<String> toggleAvailability(@RequestBody CategoryIds categoryIds) {
        categoryService.toggleAvailability(categoryIds.getIds());
        return APIResponse.<String>builder()
                .message("Categories availability toggled successfully")
                .build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<String> deleteCategories(@RequestBody CategoryIds categoryIds) {
        categoryService.delete(categoryIds.getIds());
        return APIResponse.<String>builder()
                .message("Category deleted successfully")
                .build();
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<CategoryResponse> updateCategory(@NotNull @NotEmpty @PathVariable String id, @Valid @RequestBody CategoryUpdate categoryUpdate) {
        CategoryResponse categoryResponse = categoryService.update(id, categoryUpdate);
        return APIResponse.<CategoryResponse>builder()
                .result(categoryResponse)
                .build();
    }

    @GetMapping("/admin")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<PaginationResponse<CategoryAdminResponse>> getAllCategoriesForAdmin(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isAvailable,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer itemsPerPage
    ) {

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage); // (0, 10) for page 1

        PaginationResponse<CategoryAdminResponse> paginationResponse = categoryService.getAllCategoriesAdmin(name, isAvailable, pageable);

        return APIResponse.<PaginationResponse<CategoryAdminResponse>>builder()
                .result(paginationResponse)
                .build();
    }

}
