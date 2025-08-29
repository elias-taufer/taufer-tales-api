package com.taufer.tales.tale.dto;

public record TaleResponse(Long id, String title, String author, String isbn, String description, String coverUrl,
                           Integer publishedYear, String tags, Double avgRating) {
}