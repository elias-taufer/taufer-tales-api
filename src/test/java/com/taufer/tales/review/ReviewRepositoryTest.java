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

package com.taufer.tales.review;

import com.taufer.tales.user.User;
import com.taufer.tales.tale.Tale;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.MySQLContainer;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
class ReviewRepositoryTest {

    @Container
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", mysql::getJdbcUrl);
        r.add("spring.datasource.username", mysql::getUsername);
        r.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    EntityManager em;
    @Autowired
    ReviewRepository reviews;

    @Test
    void avgRating_nullWhenNoReviews() {
        Double avg = reviews.avgRating(999L);
        assertThat(avg).isNull();
    }

    @Test
    void avgRating_averagesMultiple() {
        // users
        User u1 = new User(); u1.setUsername("bob");   u1.setPassword("{noop}pw");
        User u2 = new User(); u2.setUsername("anna");  u2.setPassword("{noop}pw");

        // one tale
        Tale t = new Tale();
        t.setTitle("T");
        t.setAuthor("A");
        t.setPublishedYear(2020);

        em.persist(u1);
        em.persist(u2);
        em.persist(t);

        Review r1 = new Review();
        r1.setUser(u1);
        r1.setTale(t);
        r1.setRating(3);
        r1.setCreatedAt(Instant.now());
        r1.setTitle("ok");
        r1.setBody("meh");

        Review r2 = new Review();
        r2.setUser(u2);
        r2.setTale(t);
        r2.setRating(5);
        r2.setCreatedAt(Instant.now());
        r2.setTitle("great");
        r2.setBody("nice");

        em.persist(r1);
        em.persist(r2);
        em.flush();

        Double avg = reviews.avgRating(t.getId());
        assertThat(avg).isBetween(3.9, 4.1);
    }
}
