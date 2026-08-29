package com.example.cafeordersystem.service;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.example.cafeordersystem.controller.dto.ProductResponse;
import com.example.cafeordersystem.entity.Product;
import com.example.cafeordersystem.repository.ProductRepository;

@DisplayName("ProductServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(productRepository);
    }

    @Nested
    @DisplayName("getAllProductsメソッドの単体テスト")
    class GetAllProductsTests {

        @DisplayName("正常系/複数件取得")
        @Test
        void getAllProducts() {
            Product coffee = new Product(1L, "ブレンドコーヒー", 450, 20, 0L);
            Product latte = new Product(2L, "カフェラテ", 520, 15, 0L);
            ProductResponse expectedCoffee = new ProductResponse(1L, "ブレンドコーヒー", 450, 20);
            ProductResponse expectedLatte = new ProductResponse(2L, "カフェラテ", 520, 15);
            List<Product> expectedProducts = List.of(coffee, latte);

            when(productRepository.findAll(any(Sort.class))).thenReturn(expectedProducts);

            List<ProductResponse> actualProducts = productService.getAllProducts();

            assertThat(actualProducts)
                .isNotNull()
                .hasSize(2)
                .containsExactly(expectedCoffee, expectedLatte);

            verify(productRepository, times(1)).findAll(any(Sort.class));
        }

        @DisplayName("正常系/0件取得")
        @Test
        void getAllProductsWhenEmpty() {
            when(productRepository.findAll(any(Sort.class))).thenReturn(List.of());

            List<ProductResponse> actualProducts = productService.getAllProducts();

            assertThat(actualProducts)
                .isNotNull()
                .isEmpty();

            verify(productRepository, times(1)).findAll(any(Sort.class));
        }
    }
}
