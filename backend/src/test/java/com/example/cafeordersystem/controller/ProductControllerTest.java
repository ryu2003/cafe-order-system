package com.example.cafeordersystem.controller;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.cafeordersystem.controller.dto.ProductResponse;
import com.example.cafeordersystem.service.ProductService;

import tools.jackson.databind.ObjectMapper;

@DisplayName("ProductControllerの単体テスト")
@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Nested
    @DisplayName("getAllProductsメソッドの単体テスト")
    class GetAllProductsTests {

        @DisplayName("複数行の商品情報をJSONで返すこと")
        @Test
        void getAllProductsWhenManyResponse() throws Exception{
            List<ProductResponse> allProductsResponse = List.of(
                new ProductResponse(1L, "ブレンドコーヒー", 450, 20),
                new ProductResponse(2L, "カフェラテ", 520, 15),
                new ProductResponse(3L, "特製モーニングセット", 780, 10)
            );
            when(productService.getAllProducts()).thenReturn(allProductsResponse);
            
            ObjectMapper objectMapper = new ObjectMapper();
            String productResponseJson = objectMapper.writeValueAsString(allProductsResponse);

            mockMvc.perform(get("/api/products"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(productResponseJson));
        }

        @DisplayName("リストが空の場合、空のJSONを返すこと")
        @Test
        void getAllProductsWhenEmpty() throws Exception {
            when(productService.getAllProducts()).thenReturn(List.of());

            mockMvc.perform(get("/api/products"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("[]"));
        }
    }

}
