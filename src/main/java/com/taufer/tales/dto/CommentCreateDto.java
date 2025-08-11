package com.taufer.tales.dto;
import jakarta.validation.constraints.*;
public record CommentCreateDto(@NotNull Long reviewId, Long parentId, @NotBlank String content) {}
