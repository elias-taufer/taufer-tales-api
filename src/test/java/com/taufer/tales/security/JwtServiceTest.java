package com.taufer.tales.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static void set(Object target, String field, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    void generate_and_validate_roundtrip() throws Exception {
        JwtService jwt = new JwtService();
        set(jwt, "secret", Base64.getEncoder().encodeToString("t9d/puu41T5Po+M3fBKwAjOES/3e6SuUm0P1dMYMoeA=".getBytes()));
        set(jwt, "expirationMs", 60_000L);

        UserDetails u = User.withUsername("alice").password("pw").roles("USER").build();
        String token = jwt.generate(u);

        assertThat(jwt.extractUsername(token)).isEqualTo("alice");
        assertThat(jwt.isValid(token, u)).isTrue();
    }
}
