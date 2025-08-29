package com.taufer.tales.security.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(@NotBlank String username, @Size(min = 8) String password) {
}