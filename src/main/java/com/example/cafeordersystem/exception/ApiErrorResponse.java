package com.example.cafeordersystem.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public record ApiErrorResponse(
    HttpStatus status,  // HTTPステータスコード
    String message, // エラーメッセージ
    LocalDateTime timeStamp // タイムスタンプ
) {}
