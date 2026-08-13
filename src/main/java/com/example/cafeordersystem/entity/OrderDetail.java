package com.example.cafeordersystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
- order_details（注文詳細ID, 注文ID, 商品ID, 数量）
*/
@Entity
@Table(name = "order_details")
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;  //注文詳細ID

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;    //注文

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; //商品

    private int quantity;   //数量
}
