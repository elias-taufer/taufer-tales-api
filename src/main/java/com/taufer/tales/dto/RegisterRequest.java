package com.taufer.tales.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(@NotBlank String username, @Email String email, @Size(min = 8) String password) {
}