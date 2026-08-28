package com.example.cafeordersystem.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.cafeordersystem.entity.Order;
import com.example.cafeordersystem.entity.OrderDetail;
import com.example.cafeordersystem.entity.Product;
import com.example.cafeordersystem.enums.OrderStatus;
import com.example.cafeordersystem.repository.OrderDetailRepository;
import com.example.cafeordersystem.repository.OrderRepository;
import com.example.cafeordersystem.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    public OrderService(
        OrderRepository orderRepository,
        OrderDetailRepository orderDetailRepository,
        ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrder(Long productId, int quantity) {
        // 商品情報の取得
        Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new IllegalArgumentException("指定された商品が存在しません: " + productId));
        
        // 在庫の減算
        product.subtractStock(quantity);
        Product saveProduct = productRepository.save(product);

        // 注文テーブルの行作成
        int totalAmount = product.getPrice() * quantity;
        Order order = new Order(null, LocalDateTime.now(), totalAmount, OrderStatus.PREPARING);
        Order saveOrder = orderRepository.save(order);

        // 注文詳細テーブルの行作成
        OrderDetail orderDetail = new OrderDetail(null, saveOrder, saveProduct, quantity);
        orderDetailRepository.save(orderDetail);

        return saveOrder;
    }
    
}
