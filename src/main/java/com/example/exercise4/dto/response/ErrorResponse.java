package com.example.exercise4.dto.response;

public record ErrorResponse(
        String timestamp,
        int status,
        String error,
        Object messages
) {
}
