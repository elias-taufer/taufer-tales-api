package com.taufer.tales.dto;

public record CommentResponse(Long id, Long reviewId, Long parentId, String username, String content,
                              String createdAt) {
}
