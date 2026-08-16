package com.example.cafeordersystem.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cafeordersystem.controller.dto.OrderRequest;
import com.example.cafeordersystem.controller.dto.OrderResponse;
import com.example.cafeordersystem.entity.Order;
import com.example.cafeordersystem.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse creatOrder(@RequestBody @Validated OrderRequest request) {
        Order saveOrder = orderService.createOrder(request.productId(), request.quantity());

        return new OrderResponse(
            saveOrder.getOrderId(),
            saveOrder.getOrderDateTime(),
            saveOrder.getTotalAmount(),
            saveOrder.getOrderStatus());
    }
}
