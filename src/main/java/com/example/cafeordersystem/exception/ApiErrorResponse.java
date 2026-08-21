package com.example.cafeordersystem.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
    HttpStatus status,  // HTTPステータスコード
    String message, // エラーメッセージ
    Map<String, String> errors,   // バリデーション エラーメッセージ
    LocalDateTime timeStamp // タイムスタンプ
) {
    public ApiErrorResponse(HttpStatus status, String message, LocalDateTime timeStamp) {
        this(status, message, null, timeStamp);
    }
}
