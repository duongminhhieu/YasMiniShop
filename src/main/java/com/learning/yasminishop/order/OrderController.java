package com.learning.yasminishop.order;

import com.learning.yasminishop.common.dto.APIResponse;
import com.learning.yasminishop.common.dto.PaginationResponse;
import com.learning.yasminishop.common.utility.PageSortUtility;
import com.learning.yasminishop.order.dto.filter.OrderFilter;
import com.learning.yasminishop.order.dto.request.OrderRequest;
import com.learning.yasminishop.order.dto.response.OrderAdminResponse;
import com.learning.yasminishop.order.dto.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final PageSortUtility pageSortUtility;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.create(orderRequest);
        return APIResponse.<OrderResponse>builder()
                .result(orderResponse)
                .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orderResponse = orderService.getAllOrderByUser();
        return APIResponse.<List<OrderResponse>>builder()
                .result(orderResponse)
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<OrderResponse> getOrderById(@PathVariable String id) {
        OrderResponse orderResponse = orderService.getOrderById(id);
        return APIResponse.<OrderResponse>builder()
                .result(orderResponse)
                .build();
    }

    @GetMapping("/admin")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<PaginationResponse<OrderAdminResponse>> getAllOrdersForAdmin(@Valid @ModelAttribute OrderFilter orderFilter) {

        Pageable pageable = pageSortUtility.createPageable(orderFilter.getPage(),
                orderFilter.getItemsPerPage(),
                orderFilter.getSortBy(),
                orderFilter.getOrderBy());

        PaginationResponse<OrderAdminResponse> orders = orderService.getAllOrders(orderFilter, pageable);

        return APIResponse.<PaginationResponse<OrderAdminResponse>>builder()
                .result(orders)
                .build();
    }

}
