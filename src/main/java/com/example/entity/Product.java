package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/*
- **products**（商品ID, 商品名, 価格, 在庫数, **version**）
 */
@Entity
@Table(name = "Product")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;  //商品ID

    private String productName; //商品名

    private int price;  //価格

    private int stock;  //在庫数

    private Long version;   //version

}
