package com.taufer.tales.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "bookshelf",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "tale_id"}),
        indexes = {@Index(columnList = "user_id"), @Index(columnList = "tale_id")}
)
public class BookshelfEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tale_id")
    private Tale tale;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 64)
    private ReadingStatus status;

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
