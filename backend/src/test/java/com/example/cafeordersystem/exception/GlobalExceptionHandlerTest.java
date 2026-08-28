package com.example.cafeordersystem.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cafeordersystem.controller.dto.OrderRequest;



@DisplayName("GlobalExceptionHandlerの単体テスト")
public class GlobalExceptionHandlerTest {
    
    private MockMvc mockMvc;

    // テスト用コントローラー
    @RestController
    static class ExceptionTestController {
        @GetMapping("/test/illegal-argument")
        void throwIllegarArgumentException() {
            throw new IllegalArgumentException("商品が存在しません");
        }

        @PostMapping("/test/not-valid")
        void throwMethodArgumentNotValidExecption(@RequestBody @Validated OrderRequest request) {
        }

        @GetMapping("/test/optimistic-lock")
        void throwOptimisticLockingFailureException() {
            throw new OptimisticLockingFailureException("もう一度やり直してください");
        }

        @GetMapping("/test/runtime-exception")
        void throwRuntimeException() {
            throw new RuntimeException("");
        }
        
        
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ExceptionTestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("IllegalArgumentException（不正なリクエスト）のハンドリング")
    class IllegalArgumentExceptionTests {

        @DisplayName("「400 Bad Request」が返されること")
        @Test
        void testHandleIllegalArgumentException() throws Exception {
            mockMvc.perform(get("/test/illegal-argument"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("400 BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("商品が存在しません"))
                    .andExpect(jsonPath("$.timeStamp").exists());
        }
    }

    @Nested
    @DisplayName("MethodArgumentNotValidException（バリデーション）のハンドリング")
    class MethodArgumentNotValidExceptionTests {

        @DisplayName("「400 Bad Request」が返されること")
        @Test
        void handleMethodArgumentNotValidException() throws Exception{
            mockMvc.perform(post("/test/not-valid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": null, \"quantity\": 0}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("400 BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("入力値が不正です"))
                    .andExpect(jsonPath("$.errors.productId").value("商品IDは必須です"))
                    .andExpect(jsonPath("$.errors.quantity").value("数量は1以上を指定してください"))
                    .andExpect(jsonPath("$.timeStamp").exists());
        }

        @DisplayName("productIdのみエラーをキャッチできること")
        @Test
        void handleProductIdOnly() throws Exception{
            mockMvc.perform(post("/test/not-valid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": null, \"quantity\": 1}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("400 BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("入力値が不正です"))
                    .andExpect(jsonPath("$.errors.productId").value("商品IDは必須です"))
                    .andExpect(jsonPath("$.errors.quantity").doesNotExist())
                    .andExpect(jsonPath("$.timeStamp").exists());
        }
    }

    @Nested
    @DisplayName("OptimisticLockingFailureException（楽観的ロック）のハンドリング")
    class OptimisticLockingFailureExceptionTests {

        @DisplayName("「409 Conflict」が返されること")
        @Test
        void handleOptimisticLockingFailureException() throws Exception {
            mockMvc.perform(get("/test/optimistic-lock"))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value("409 CONFLICT"))
                    .andExpect(jsonPath("$.message").value("もう一度やり直してください"))
                    .andExpect(jsonPath("$.timeStamp").exists());
        }
    }

    @Nested
    @DisplayName("Exception（予期せぬ例外）のハンドリング")
    class ExceptionTests {

        @DisplayName("「500 Internal Server Error」が返されること")
        @Test
        void handleException() throws Exception {
            mockMvc.perform(get("/test/runtime-exception"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.status").value("500 INTERNAL_SERVER_ERROR"))
                    .andExpect(jsonPath("$.message").value("予期せぬエラーが発生しました"))
                    .andExpect(jsonPath("$.timeStamp").exists());
        }
    }
}
