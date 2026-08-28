package com.example.cafeordersystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.cafeordersystem.entity.Product;
import com.example.cafeordersystem.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }
    
}
