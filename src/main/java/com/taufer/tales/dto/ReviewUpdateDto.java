package com.taufer.tales.dto;

import jakarta.validation.constraints.*;

public record ReviewUpdateDto(@Min(1) @Max(5) int rating, String title, String body) {
}
