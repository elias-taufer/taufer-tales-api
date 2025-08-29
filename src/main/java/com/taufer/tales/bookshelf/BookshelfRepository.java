package com.taufer.tales.bookshelf;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookshelfRepository extends JpaRepository<BookshelfEntry, Long> {
    List<BookshelfEntry> findByUser_Id(Long userId);
    List<BookshelfEntry> findByUser_IdAndStatus(Long userId, ReadingStatus status);
    Optional<BookshelfEntry> findByUser_IdAndTale_Id(Long userId, Long taleId);
}
