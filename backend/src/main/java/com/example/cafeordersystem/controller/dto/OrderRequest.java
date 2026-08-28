package com.example.cafeordersystem.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public record OrderRequest(
    @NotNull(message = "商品IDは必須です")
    Long productId, // 商品ID

    @Min(value = 1, message = "数量は1以上を指定してください")
    int quantity    // 数量
) {}
