package com.example.cafeordersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cafeordersystem.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>{
    
}
