package com.taufer.tales.mapper;

import com.taufer.tales.domain.Comment;
import com.taufer.tales.dto.CommentResponse;

public final class CommentMapper {
    private CommentMapper() {}
    public static CommentResponse toResponse(Comment c) {
        return new CommentResponse(
                c.getId(),
                c.getReview().getId(),
                c.getParent() == null ? null : c.getParent().getId(),
                c.getUser().getUsername(),
                c.getContent(),
                c.getCreatedAt().toString()
        );
    }
}
