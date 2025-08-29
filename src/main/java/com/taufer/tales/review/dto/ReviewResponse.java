package com.taufer.tales.review.dto;

public record ReviewResponse(
        Long id,
        Long taleId,
        String username,
        int rating,
        String title,
        String body,
        String createdAt
) { }