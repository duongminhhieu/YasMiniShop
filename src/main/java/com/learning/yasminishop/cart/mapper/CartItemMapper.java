package com.learning.yasminishop.cart.mapper;

import com.learning.yasminishop.cart.dto.request.CartItemRequest;
import com.learning.yasminishop.cart.dto.response.CartItemResponse;
import com.learning.yasminishop.common.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "product" , ignore = true)
    @Mapping(target = "user" , ignore = true)
    CartItem toCartItem(CartItemRequest cartItemRequest);

    CartItemResponse toCartResponse(CartItem cartItem);
}
