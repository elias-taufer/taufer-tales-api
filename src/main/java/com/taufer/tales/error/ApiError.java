package com.taufer.tales.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<ApiFieldError> fieldErrors // optional; null when not a validation error
) {
    // ---- Factories ----

    public static ApiError of(HttpStatus status, String message, String path) {
        return new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                null
        );
    }

    public static ApiError of(HttpStatus status, String message, String path, List<ApiFieldError> fieldErrors) {
        return new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                fieldErrors
        );
    }

    // Convenience helpers matching your previous API
    public static ApiError unauthorized(String message, String path) {
        return of(HttpStatus.UNAUTHORIZED, message, path);
    }

    public static ApiError forbidden(String message, String path) {
        return of(HttpStatus.FORBIDDEN, message, path);
    }

    public static ApiError badRequest(String message, String path) {
        return of(HttpStatus.BAD_REQUEST, message, path);
    }
}
