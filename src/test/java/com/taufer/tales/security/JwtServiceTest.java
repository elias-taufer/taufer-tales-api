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
