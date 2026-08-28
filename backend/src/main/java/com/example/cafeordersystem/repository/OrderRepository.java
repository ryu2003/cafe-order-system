package com.example.cafeordersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cafeordersystem.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
