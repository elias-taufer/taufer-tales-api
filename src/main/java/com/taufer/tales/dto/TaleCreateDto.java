package com.taufer.tales.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

public record TaleCreateDto(
        @NotBlank String title,
        @NotBlank String author,
        @Pattern(regexp="\\d{10}|\\d{13}", message="ISBN must be 10 or 13 digits") String isbn,
        @Size(max=2000) String description,
        @URL String coverUrl,
        Integer publishedYear,
        @Size(max=200) String tags
) {
}
