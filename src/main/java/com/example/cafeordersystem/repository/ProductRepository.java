package com.example.cafeordersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cafeordersystem.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    
}
