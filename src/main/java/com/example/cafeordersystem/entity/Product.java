package com.example.cafeordersystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

/*
- **products**（商品ID, 商品名, 価格, 在庫数, **version**）
 */
@Entity
@Table(name = "product")
@Data
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;  //商品ID

    private String productName; //商品名

    private int price;  //価格

    private int stock;  //在庫数

    @Version
    private Long version;  // 排他制御用バージョン

}
