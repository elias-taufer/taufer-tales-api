package com.taufer.tales.service;

import com.taufer.tales.domain.Comment;
import com.taufer.tales.domain.Review;
import com.taufer.tales.domain.User;
import com.taufer.tales.dto.CommentCreateDto;
import com.taufer.tales.dto.CommentResponse;
import com.taufer.tales.mapper.CommentMapper;
import com.taufer.tales.repo.CommentRepository;
import com.taufer.tales.repo.ReviewRepository;
import com.taufer.tales.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository comments;
    private final ReviewRepository reviews;
    private final UserRepository users;

    public List<CommentResponse> listByReview(Long reviewId) {
        return comments.findByReview_IdOrderByCreatedAtAsc(reviewId).stream()
                .map(CommentMapper::toResponse)
                .toList();
    }

    @Transactional
    public CommentResponse create(CommentCreateDto d, Authentication auth) {
        Review review = reviews.findById(d.reviewId()).orElseThrow();
        User user = users.findByUsername(auth.getName()).orElseThrow();
        Comment parent = d.parentId() == null ? null : comments.findById(d.parentId()).orElseThrow();
        Comment c = Comment.builder()
                .review(review)
                .user(user)
                .parent(parent)
                .content(d.content())
                .build();
        c = comments.save(c);
        return CommentMapper.toResponse(c);
    }
}