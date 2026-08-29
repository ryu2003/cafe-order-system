package com.example.cafeordersystem.controller;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.cafeordersystem.controller.dto.OrderRequest;
import com.example.cafeordersystem.controller.dto.OrderResponse;
import com.example.cafeordersystem.enums.OrderStatus;
import com.example.cafeordersystem.service.OrderService;

import tools.jackson.databind.ObjectMapper;

@DisplayName("OrderControllerの単体テスト")
@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    
    @Nested
    @DisplayName("createOrderメソッドの単体テスト")
    class CreateOrderTests {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private OrderService orderService;

        @AfterEach
        void tearDown() {
            verifyNoMoreInteractions(orderService);
        }

        @DisplayName("OrderのJSONが返されること")
        @Test
        void createOrderWhenOrderResponse() throws Exception {
            OrderRequest orderRequest = new OrderRequest(1L, 2);

            LocalDateTime dateTime = LocalDateTime.now();
            OrderResponse saveOrder = new OrderResponse(1L, dateTime, 900, OrderStatus.PREPARING);
            when(orderService.createOrder(1L, 2)).thenReturn(saveOrder);

            OrderResponse orderResponse = new OrderResponse(1L, dateTime, 900, OrderStatus.PREPARING);
            ObjectMapper objectMapper = new ObjectMapper();
            String orderResponseJson = objectMapper.writeValueAsString(orderResponse);

            mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(orderResponseJson));
            
            verify(orderService, times(1)).createOrder(1L, 2);
        }

        @DisplayName("productIdフィールドのバリデーション異常、orderServiceのメソッドが呼ばれないこと")
        @Test
        void createOrderWhenValidErrorOfProductId() throws Exception {
            mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": null, \"quantity\": 1}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("400 BAD_REQUEST"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @DisplayName("quantityフィールドのバリデーション異常、orderServiceのメソッドが呼ばれないこと")
        @Test
        void createOrderWhenValidErrorOfQuantity() throws Exception {
            mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": 1, \"quantity\": 0}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("400 BAD_REQUEST"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @DisplayName("全フィールドのバリデーション異常、orderServiceのメソッドが呼ばれないこと")
        @Test
        void createOrderWhenValidErrorsOfAll() throws Exception {
            mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": null, \"quantity\": 0}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("400 BAD_REQUEST"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

    }
}
