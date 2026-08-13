package com.example.cafeordersystem.entity;

import java.time.LocalDateTime;

import com.example.cafeordersystem.enums.OrderStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
- orders（注文ID, 注文日時, 合計金額, ステータス）
*/
@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;   //注文ID

    private LocalDateTime orderDateTime;    //注文日時

    private int totalAmount;    //合計金額

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;    //ステータス
}
