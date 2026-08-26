package com.example.cafeordersystem.service;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.cafeordersystem.entity.Product;
import com.example.cafeordersystem.repository.ProductRepository;

@DisplayName("ProductServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
public class TestProductService {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;
    
    @Nested
    @DisplayName("getAllProductsメソッドの単体テスト")
    class GetAllProductsTests {

        @DisplayName("正常系/複数件取得")
        @Test
        void testGetAllProducts() {
            Product coffee = new Product(1L, "ブランドコーヒー", 450, 20, 0L);
            Product latte = new Product(2L, "カフェラテ", 520, 15, 0L);
            List<Product> expectedProducts = List.of(coffee, latte);

            when(productRepository.findAll()).thenReturn(expectedProducts);

            List<Product> actualProducts = productService.getAllProducts();

            assertThat(actualProducts)
                .isNotNull()
                .hasSize(2)
                .containsExactly(coffee, latte);

            verify(productRepository, times(1)).findAll();
            verifyNoMoreInteractions(productRepository);
        }

        @DisplayName("正常系/0件取得")
        @Test
        void testGetAllProductsWhenEmpty() {
            when(productRepository.findAll()).thenReturn(Collections.emptyList());

            List<Product> actualProducts = productService.getAllProducts();

            assertThat(actualProducts)
                .isNotNull()
                .isEmpty();

            verify(productRepository, times(1)).findAll();
            verifyNoMoreInteractions(productRepository);
        }
    }
}
