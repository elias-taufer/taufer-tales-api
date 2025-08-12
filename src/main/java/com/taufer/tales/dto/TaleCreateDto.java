package com.taufer.tales.dto;

import jakarta.validation.constraints.*;

public record TaleCreateDto(@NotBlank String title, @NotBlank String author, String isbn, String description,
                            String coverUrl, Integer publishedYear, String tags) {
}
