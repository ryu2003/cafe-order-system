package com.example.cafeordersystem.service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

import com.example.cafeordersystem.entity.Order;
import com.example.cafeordersystem.entity.OrderDetail;
import com.example.cafeordersystem.entity.Product;
import com.example.cafeordersystem.enums.OrderStatus;
import com.example.cafeordersystem.repository.OrderDetailRepository;
import com.example.cafeordersystem.repository.OrderRepository;
import com.example.cafeordersystem.repository.ProductRepository;

@DisplayName("OrderServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(productRepository, orderRepository, orderDetailRepository);
    }

    @Nested
    @DisplayName("createOrderの単体テスト")
    class CreateOrderTests {

        @DisplayName("存在しない商品IDを指定した場合、IllegalArgumentException がスローされること")
        @Test
        void createOrderWhenProductNotFound() {
            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.createOrder(1L, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("指定された商品が存在しません: 1");

            verify(productRepository, times(1)).findById(1L);
            verifyNoMoreInteractions(productRepository);
            verifyNoMoreInteractions(orderRepository);
            verifyNoMoreInteractions(orderDetailRepository);
        }

        @DisplayName("注文処理が正常に完了すること")
        @Test
        void successCreateOrder() {
            Product coffee = new Product(1L, "ブレンドコーヒー", 450, 20, 0L);

            when(productRepository.findById(1L)).thenReturn(Optional.of(coffee));
            when(productRepository.save(any(Product.class))).thenAnswer(returnsFirstArg());
            when(orderRepository.save(any(Order.class))).thenAnswer(returnsFirstArg());
            when(orderDetailRepository.save(any(OrderDetail.class))).thenAnswer(returnsFirstArg());

            Order expectedOrder = orderService.createOrder(1L, 2);

            assertThat(expectedOrder)
                .isNotNull()
                .hasFieldOrPropertyWithValue("totalAmount", 900)
                .hasFieldOrPropertyWithValue("orderStatus", OrderStatus.PREPARING);
            
            assertThat(coffee.getStock()).isEqualTo(18);
            
            verify(productRepository, times(1)).findById(1L);
            verify(productRepository, times(1)).save(any(Product.class));
            verify(orderRepository, times(1)).save(any(Order.class));
            verify(orderDetailRepository, times(1)).save(any(OrderDetail.class));
        }

        @DisplayName("注文数が現在庫をこえている場合、IllegalArgmentExceptionがスローされること")
        @Test
        void quantityExceedsStock() {
            Product coffee = new Product(1L, "ブレンドコーヒー", 450, 2, 0L);
            when(productRepository.findById(1L)).thenReturn(Optional.of(coffee));

            assertThatThrownBy(() -> orderService.createOrder(1L, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("在庫不足");
            
            verify(productRepository, times(1)).findById(1L);
        }

        @DisplayName("楽観的ロック（排他制御）の競合が発生した場合、OptimisticLockingFailureExceptionがスローされること")
        @Test
        void optimisticLockConflictOccurs() {
            Product coffee = new Product(1L, "ブレンドコーヒー", 450, 2, 0L);
            when(productRepository.findById(1L)).thenReturn(Optional.of(coffee));
            when(productRepository.save(any(Product.class))).thenThrow(new OptimisticLockingFailureException("競合エラー"));

            assertThatThrownBy(() -> orderService.createOrder(1L, 1))
                .isInstanceOf(OptimisticLockingFailureException.class)
                .hasMessage("競合エラー");
            
            verify(productRepository, times(1)).findById(1L);
            verify(productRepository, times(1)).save(any(Product.class));
        }

    }
}
