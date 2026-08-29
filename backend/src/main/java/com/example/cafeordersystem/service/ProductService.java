package com.example.cafeordersystem.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.cafeordersystem.controller.dto.ProductResponse;
import com.example.cafeordersystem.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, "productId"))
                .stream()
                .map(ProductResponse::from)
                .toList();
    }
    
}
