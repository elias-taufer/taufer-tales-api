package com.taufer.tales.tale.dto;

public record TaleUpdateDto(String title, String author, String isbn, String description, String coverUrl,
                            Integer publishedYear, String tags) {
}