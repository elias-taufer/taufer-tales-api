package com.taufer.tales.repo;

import com.taufer.tales.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByReview_IdOrderByCreatedAtAsc(Long reviewId);

    List<Comment> findByParent_IdOrderByCreatedAtAsc(Long parentId);
}