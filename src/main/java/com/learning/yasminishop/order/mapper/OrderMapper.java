package com.learning.yasminishop.order.mapper;

import com.learning.yasminishop.common.entity.CartItem;
import com.learning.yasminishop.common.entity.Order;
import com.learning.yasminishop.common.entity.OrderAddress;
import com.learning.yasminishop.common.entity.OrderItem;
import com.learning.yasminishop.order.dto.request.OrderAddressRequest;
import com.learning.yasminishop.order.dto.request.OrderRequest;
import com.learning.yasminishop.order.dto.response.OrderAddressResponse;
import com.learning.yasminishop.order.dto.response.OrderItemResponse;
import com.learning.yasminishop.order.dto.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toOrder(OrderRequest orderRequest);
    OrderAddress toOrderAddress(OrderAddressRequest orderAddressRequest);

    @Mapping(target = "id", ignore = true)
    OrderItem toOrderItem(CartItem cartItem);

    OrderResponse toOrderResponse(Order order);
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
    OrderAddressResponse toOrderAddressResponse(OrderAddress orderAddress);
}
