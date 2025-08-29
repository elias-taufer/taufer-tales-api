package com.taufer.tales.security.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimitFilter extends OncePerRequestFilter {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket resolveBucket(String key) {
        return buckets.computeIfAbsent(key, k ->
                Bucket.builder()
                        .addLimit(Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)))) // 5 req/min
                        .addLimit(Bandwidth.classic(40, Refill.greedy(40, Duration.ofHours(1)))) // 20 req/hour
                        .build());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        String p = req.getRequestURI();
        return !(p.equals("/api/auth/login") || p.equals("/api/auth/register"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String ip = Optional.ofNullable(req.getHeader("X-Forwarded-For"))
                .map(h -> h.split(",")[0].trim())
                .orElseGet(req::getRemoteAddr);

        Bucket bucket = resolveBucket(ip);
        if (bucket.tryConsume(1)) {
            chain.doFilter(req, res);
        } else {
            res.setStatus(429);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\":\"Too Many Requests\"}");
        }
    }
}
