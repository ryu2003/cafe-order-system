package com.example.cafeordersystem.controller.dto;

public record ProductResponse(
    Long productId, // 商品ID
    String productName, // 商品名
    int price,  // 価格
    int stock   // 在庫数
){}
