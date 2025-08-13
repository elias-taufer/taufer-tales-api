package com.taufer.tales.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = {@Index(columnList = "review_id"), @Index(columnList = "parent_id")})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private Review review;
    @ManyToOne(optional = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent; // nullable
    @Column(nullable = false, length = 4000)
    private String content;
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }
}