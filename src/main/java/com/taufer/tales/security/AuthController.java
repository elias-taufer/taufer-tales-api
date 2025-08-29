package com.taufer.tales.security;

import com.taufer.tales.security.dto.AuthResponse;
import com.taufer.tales.security.dto.LoginRequest;
import com.taufer.tales.security.dto.RegisterRequest;
import com.taufer.tales.security.ratelimit.AuthRateLimiter;
import com.taufer.tales.user.Role;
import com.taufer.tales.user.User;
import com.taufer.tales.user.UserRepository;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthRateLimiter rateLimiter;
    private final UserRepository users;
    private final PasswordEncoder enc;
    private final AuthenticationManager auth;
    private final JwtService jwt;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest req) {
        if (users.existsByUsername(req.username()))
            return ResponseEntity.badRequest().build();
        User u = User.builder().username(req.username()).password(enc.encode(req.password())).build();
        u.getRoles().add(Role.USER);
        users.save(u);
        String token = jwt.generate(u);
        return ResponseEntity.ok(new AuthResponse(token, u.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req, HttpServletRequest http) {
        rateLimiter.check(req.username(), http);
        auth.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        var u = users.findByUsername(req.username()).orElseThrow();
        return ResponseEntity.ok(new AuthResponse(jwt.generate(u), u.getUsername()));
    }
}

