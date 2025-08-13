package com.taufer.tales.dto;

import jakarta.validation.constraints.*;

public record ReviewCreateDto(@NotNull Long taleId, @Min(1) @Max(5) int rating, String title, String body) {
}