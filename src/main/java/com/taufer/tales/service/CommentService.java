package com.taufer.tales.service;

import com.taufer.tales.domain.*;
import lombok.extern.slf4j.Slf4j;
import com.taufer.tales.dto.*;
import com.taufer.tales.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository comments;
    private final ReviewRepository reviews;
    private final UserRepository users;

    public List<CommentResponse> listByReview(Long reviewId) {
        return comments.findByReview_IdOrderByCreatedAtAsc(reviewId).stream().map(c -> new CommentResponse(c.getId(), c.getReview().getId(), c.getParent() == null ? null : c.getParent().getId(), c.getUser().getUsername(), c.getContent(), c.getCreatedAt().toString())).toList();
    }

    public CommentResponse create(CommentCreateDto d, Authentication auth) {
        Review r = reviews.findById(d.reviewId()).orElseThrow();
        User u = users.findByUsername(auth.getName()).orElseThrow();
        Comment parent = (d.parentId() == null) ? null : comments.findById(d.parentId()).orElseThrow();
        Comment c = comments.save(Comment.builder().review(r).user(u).parent(parent).content(d.content()).build());
        return new CommentResponse(c.getId(), r.getId(), parent == null ? null : parent.getId(), u.getUsername(), c.getContent(), c.getCreatedAt().toString());
    }
}
