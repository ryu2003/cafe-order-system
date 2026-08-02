package com.example.cafeordersystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*
- order_details（注文詳細ID, 注文ID, 商品ID, 数量）
*/
@Entity
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;  //注文詳細ID

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order orderId;    //注文ID

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product productId; //商品ID

    private int quantity;   //数量
}
