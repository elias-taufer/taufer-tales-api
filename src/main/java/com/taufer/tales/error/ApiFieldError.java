package com.taufer.tales.error;

public record ApiFieldError(
        String field,
        String message,
        Object rejectedValue,
        String code
) {}