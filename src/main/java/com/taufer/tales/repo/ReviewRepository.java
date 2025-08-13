package com.taufer.tales.repo;

import com.taufer.tales.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTale_IdOrderByCreatedAtDesc(Long taleId);
    Optional<Review> findByUserIdAndTaleId(Long userId, Long taleId);

    @Query("select avg(r.rating) from Review r where r.tale.id = :taleId")
    Double avgRating(Long taleId);
}