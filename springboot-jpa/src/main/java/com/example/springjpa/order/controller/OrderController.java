package com.example.springjpa.order.controller;

import com.example.springjpa.order.ApiResponse;
import com.example.springjpa.order.dto.OrderDto;
import com.example.springjpa.order.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ApiResponse<String> createOrder(@RequestBody OrderDto orderDto){
        return ApiResponse.ok(orderService.save(orderDto));
    }

    @GetMapping("/orders")
    public ApiResponse<List<OrderDto>> getOrders(Pageable pageable){
        return ApiResponse.ok(orderService.getOrders(pageable).getContent());
    }

    @GetMapping("/orders/{uuid}")
    public ApiResponse<OrderDto> getOrder(@PathVariable String uuid){
        return ApiResponse.ok(orderService.getOrderById(uuid));
    }

}
