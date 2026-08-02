package com.example.cafeordersystem.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import com.example.cafeordersystem.service.ProductService;
import com.example.cafeordersystem.entity.Product;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    public List<Product> getMappingProductAll() {
        return productService.getAllProduct();
    }
}
