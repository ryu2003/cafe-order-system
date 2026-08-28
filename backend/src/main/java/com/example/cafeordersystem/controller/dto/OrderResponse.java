package com.example.cafeordersystem.controller.dto;

import java.time.LocalDateTime;

import com.example.cafeordersystem.enums.OrderStatus;

public record OrderResponse(
    Long orderId,   // 注文ID
    LocalDateTime orderDateTime,    // 注文日時
    int totalAmount,    // 合計金額
    OrderStatus orderStatus // ステータス
) {}
