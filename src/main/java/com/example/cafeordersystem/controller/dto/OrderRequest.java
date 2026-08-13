package com.example.cafeordersystem.controller.dto;

public record OrderRequest(
    Long productId, // 商品ID
    int quantity    // 数量
) {}
