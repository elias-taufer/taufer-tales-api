package com.taufer.tales.authgateway;

import com.taufer.tales.domain.Role;
import lombok.extern.slf4j.Slf4j;
import com.taufer.tales.domain.User;
import com.taufer.tales.dto.*;
import com.taufer.tales.repo.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder enc;
    private final AuthenticationManager auth;
    private final JwtService jwt;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest req) {
        if (users.existsByUsername(req.username()) || users.existsByEmail(req.email()))
            return ResponseEntity.badRequest().build();
        User u = User.builder().username(req.username()).email(req.email()).password(enc.encode(req.password())).build();
        u.getRoles().add(Role.USER);
        users.save(u);
        String token = jwt.generate(u);
        return ResponseEntity.ok(new AuthResponse(token, u.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req) {
        auth.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        var u = users.findByUsername(req.username()).orElseThrow();
        return ResponseEntity.ok(new AuthResponse(jwt.generate(u), u.getUsername()));
    }
}


