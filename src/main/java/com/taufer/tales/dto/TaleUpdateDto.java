package com.taufer.tales.dto;
public record TaleUpdateDto(String title, String author, String isbn, String description, String coverUrl, Integer publishedYear, String tags) {}
