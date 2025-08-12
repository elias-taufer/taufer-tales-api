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
@Table(
        indexes = {@Index(columnList = "tale_id"), @Index(columnList = "user_id")},
        uniqueConstraints = @UniqueConstraint(columnNames = {"tale_id","user_id"})
)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private Tale tale;
    @ManyToOne(optional = false)
    private User user;
    @Column(nullable = false)
    private int rating; // 1..5
    private String title;
    @Column(length = 8000)
    private String body;
    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}
