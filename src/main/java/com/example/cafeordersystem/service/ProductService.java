package com.example.cafeordersystem.service;

import org.springframework.stereotype.Service;

import java.util.List;

import com.example.cafeordersystem.repository.ProductRepository;
import com.example.cafeordersystem.entity.Product;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProduct() {
        return this.productRepository.findAll();
    }
    
}
