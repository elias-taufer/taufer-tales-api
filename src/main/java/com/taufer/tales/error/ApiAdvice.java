package com.taufer.tales.error;

import com.taufer.tales.security.ratelimit.AuthRateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiAdvice {
    @ExceptionHandler(AuthRateLimiter.TooManyRequestsException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public Map<String,String> tooMany() { return Map.of("error","Too Many Requests"); }
}