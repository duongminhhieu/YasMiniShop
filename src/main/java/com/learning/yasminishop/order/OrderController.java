package com.learning.yasminishop.order;

import com.learning.yasminishop.common.dto.APIResponse;
import com.learning.yasminishop.order.dto.request.OrderRequest;
import com.learning.yasminishop.order.dto.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;


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




}
