package com.example.cafeordersystem.exception;

import java.time.LocalDateTime;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    // アプリケーションエラーをキャッチ
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> HandleIllegalArgumentException(IllegalArgumentException e) {
        ApiErrorResponse error = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            e.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // バリデーションエラーをキャッチ
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> HandleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiErrorResponse error = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            e.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // @Versionエラーをキャッチ
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ApiErrorResponse> HandleOptimisticLockingFailureException(OptimisticLockingFailureException e) {
        ApiErrorResponse error = new ApiErrorResponse(
            HttpStatus.CONFLICT,
            e.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // その他のエラーをキャッチ
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> HandleException(Exception e) {
        ApiErrorResponse error = new ApiErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "予期せぬエラーが発生しました",
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
