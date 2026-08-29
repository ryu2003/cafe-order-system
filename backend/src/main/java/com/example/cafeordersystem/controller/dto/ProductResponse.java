package com.example.cafeordersystem.controller.dto;

import com.example.cafeordersystem.entity.Product;

public record ProductResponse(
    Long productId, // 商品ID
    String productName, // 商品名
    int price,  // 価格
    int stock   // 在庫数
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getProductId(),
            product.getProductName(),
            product.getPrice(),
            product.getStock()
        );
    }
}
