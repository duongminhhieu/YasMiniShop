package com.learning.yasminishop.order;

import com.learning.yasminishop.cart.CartItemRepository;
import com.learning.yasminishop.common.entity.CartItem;
import com.learning.yasminishop.common.entity.Order;
import com.learning.yasminishop.common.entity.OrderItem;
import com.learning.yasminishop.common.entity.User;
import com.learning.yasminishop.common.enumeration.EOrderStatus;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.common.exception.ErrorCode;
import com.learning.yasminishop.order.dto.request.OrderRequest;
import com.learning.yasminishop.order.dto.response.OrderResponse;
import com.learning.yasminishop.order.mapper.OrderMapper;
import com.learning.yasminishop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    private final OrderMapper orderMapper;


    @Transactional
    @PreAuthorize("hasRole('USER')")
    public OrderResponse create(OrderRequest orderRequest) {

        // get the user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // get the cart items of the user
        List<CartItem> cartItems = cartItemRepository.findAllByUserOrderByLastModifiedDateDesc(user);
        Set<String> cartItemRequestIds = orderRequest.getCartItemIds();
        for (CartItem cartItem : cartItems) {
            if (!cartItemRequestIds.contains(cartItem.getId())) {
                throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
            }
        }

        List<CartItem> cartItemsToOrder = cartItemRepository.findAllById(cartItemRequestIds);

        // create the order
        Order order = createAOrder(orderRequest, user, cartItemsToOrder);

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }


    @Transactional
    @PreAuthorize("hasRole('USER')")
    public List<OrderResponse> getAllOrderByUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return orderRepository.findAllByUserOrderByLastModifiedDateDesc(user)
                .stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }


    private Order createAOrder(OrderRequest orderRequest, User user, List<CartItem> cartItemsToOrder) {
        Order order = orderMapper.toOrder(orderRequest);
        order.setUser(user);
        order = orderRepository.save(order);

        Order finalOrder = order;
        Set<OrderItem> orderItems = cartItemsToOrder
                .stream()
                .map(item -> {
                    OrderItem orderItem = orderMapper.toOrderItem(item);
                    orderItem.setOrder(finalOrder);
                    return orderItem;
                })
                .collect(Collectors.toSet());

        BigDecimal totalPrice = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalQuantity = orderItems.stream()
                .map(OrderItem::getQuantity)
                .reduce(0, Integer::sum);

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);
        order.setTotalQuantity(totalQuantity);
        order.setStatus(EOrderStatus.PENDING);

        // delete the cart items
        cartItemRepository.deleteAll(cartItemsToOrder);

        return order;
    }


}
