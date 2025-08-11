package com.taufer.tales.dto;
import jakarta.validation.constraints.*;
public record LoginRequest(@NotBlank String username, @NotBlank String password) {}
