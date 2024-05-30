package com.learning.yasminishop.cart;


import com.learning.yasminishop.cart.dto.request.CartItemIds;
import com.learning.yasminishop.cart.dto.request.CartItemRequest;
import com.learning.yasminishop.cart.dto.request.CartItemUpdate;
import com.learning.yasminishop.cart.dto.response.CartItemResponse;
import com.learning.yasminishop.common.dto.APIResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@Slf4j
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<CartItemResponse> createCart(@Valid @RequestBody CartItemRequest cartItemRequest) {
        CartItemResponse cartItemResponse = cartItemService.create(cartItemRequest);
        return APIResponse.<CartItemResponse>builder()
                .result(cartItemResponse)
                .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<List<CartItemResponse>> getAllCarts() {
        List<CartItemResponse> cartItemResponses = cartItemService.getAll();

        return APIResponse.<List<CartItemResponse>>builder()
                .result(cartItemResponses)
                .build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<CartItemResponse> updateCart(@PathVariable String id, @Valid @RequestBody CartItemUpdate cartItemUpdate) {
        CartItemResponse cartItemResponses = cartItemService.update(id, cartItemUpdate);

        return APIResponse.<CartItemResponse>builder()
                .result(cartItemResponses)
                .build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<String> deleteCart(@Valid @RequestBody CartItemIds cartItemIds) {
        cartItemService.delete(cartItemIds.getIds());

        return APIResponse.<String>builder()
                .message("Cart items deleted successfully")
                .build();
    }

    @GetMapping("/get-by-ids")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<List<CartItemResponse>> getCartItemsByIds(@RequestParam List<String> ids) {

        List<CartItemResponse> cartItemResponses = cartItemService.getCartByIds(ids);

        return APIResponse.<List<CartItemResponse>>builder()
                .result(cartItemResponses)
                .build();
    }

}
