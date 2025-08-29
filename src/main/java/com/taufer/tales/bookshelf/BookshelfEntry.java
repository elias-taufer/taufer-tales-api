package com.taufer.tales.bookshelf;

import com.taufer.tales.tale.Tale;
import com.taufer.tales.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tale_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
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
