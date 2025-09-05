/*
 *  Copyright 2025 Elias Taufer
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.taufer.tales.security.ratelimit;

import io.github.bucket4j.*;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthRateLimiter {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private static Bucket newBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(5,  Refill.greedy(5,  Duration.ofMinutes(1))))  // 5/min
                .addLimit(Bandwidth.classic(20, Refill.greedy(20, Duration.ofHours(1))))    // 20/hour
                .build();
    }
    private Bucket bucket(String key) { return buckets.computeIfAbsent(key, k -> newBucket()); }

    public void check(String username, HttpServletRequest req) {
        final String ip = clientIp(req);
        final String userKey = "u:" + (username == null ? "" : username.toLowerCase());
        final String ipKey   = "ip:" + ip;

        if (!bucket(userKey).tryConsume(1) || !bucket(ipKey).tryConsume(1)) {
            throw new TooManyRequestsException();
        }
    }

    private String clientIp(HttpServletRequest req) {
        String xff = req.getHeader("X-Forwarded-For");
        return (xff != null && !xff.isBlank()) ? xff.split(",")[0].trim() : req.getRemoteAddr();
    }

    public static class TooManyRequestsException extends RuntimeException {}
}