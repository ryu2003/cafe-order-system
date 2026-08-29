package com.example.cafeordersystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cafeordersystem.controller.dto.ProductResponse;
import com.example.cafeordersystem.entity.Product;
import com.example.cafeordersystem.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }
}
