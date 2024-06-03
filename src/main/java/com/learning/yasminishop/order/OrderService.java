package com.learning.yasminishop.order;

import com.learning.yasminishop.cart.CartItemRepository;
import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.common.entity.*;
import com.learning.yasminishop.common.enumeration.EOrderStatus;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.common.exception.ErrorCode;
import com.learning.yasminishop.notification.NotificationService;
import com.learning.yasminishop.order.dto.filter.OrderFilter;
import com.learning.yasminishop.order.dto.request.OrderRequest;
import com.learning.yasminishop.order.dto.response.OrderAdminResponse;
import com.learning.yasminishop.order.dto.response.OrderResponse;
import com.learning.yasminishop.order.mapper.OrderMapper;
import com.learning.yasminishop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    private final NotificationService notificationService;

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

        // Extract IDs from cartItems
        Set<String> cartItemIds = cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toSet());

        // Check if all cartItemRequestIds are in cartItemIds
        for (String requestId : cartItemRequestIds) {
            if (!cartItemIds.contains(requestId)) {
                throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
            }
        }

        List<CartItem> cartItemsToOrder = cartItemRepository.findAllById(cartItemRequestIds);

        // create the order
        Order order = createAOrder(orderRequest, user, cartItemsToOrder);

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }


    @PreAuthorize("hasRole('USER')")
    public List<OrderResponse> getAllOrderByUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return orderRepository.findAllByUserOrderByLastModifiedDateDesc(user)
                .stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PaginationResponse<OrderAdminResponse> getAllOrders(OrderFilter orderFilter, Pageable pageable) {

        Page<Order> orders = orderRepository.findAll(
                Specification.where(OrderSpecifications.hasStatus(orderFilter.getStatus()))
                , pageable);

        return PaginationResponse.<OrderAdminResponse>builder()
                .page(pageable.getPageNumber() + 1)
                .total(orders.getTotalElements())
                .itemsPerPage(pageable.getPageSize())
                .data(orders.map(orderMapper::toOrderAdminResponse).toList())
                .build();
    }

    @PreAuthorize("hasRole('USER')")
    public OrderResponse getOrderById(String id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Order order = orderRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new AppException(ErrorCode.ORDER_NOT_FOUND)
        );

        return orderMapper.toOrderResponse(order);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public OrderAdminResponse getOrderByIdForAdmin(String id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.ORDER_NOT_FOUND)
        );
        return orderMapper.toOrderAdminResponse(order);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void updateOrderStatus(String id, String status) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.ORDER_NOT_FOUND)
        );

        order.setStatus(EOrderStatus.valueOf(status));
        orderRepository.save(order);

        sendNotification(order);
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

    private void sendNotification(Order order) {
        OrderItem orderItem = order.getOrderItems().stream().findFirst().orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        Notification notification = Notification.builder()
                .user(order.getUser())
                .title("Your order status has been updated")
                .content("Your order status has been updated to " + order.getStatus())
                .isRead(false)
                .thumbnail(orderItem.getProduct().getThumbnail())
                .build();

        notificationService.createNotification(notification);
    }

}
