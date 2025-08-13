package com.taufer.tales.mapper;

import com.taufer.tales.domain.Review;
import com.taufer.tales.dto.ReviewResponse;

public final class ReviewMapper {
    private ReviewMapper() {}
    public static ReviewResponse toResponse(Review r) {
        return new ReviewResponse(
                r.getId(),
                r.getTale().getId(),
                r.getUser().getUsername(),
                r.getRating(),
                r.getTitle(),
                r.getBody(),
                r.getCreatedAt().toString()
        );
    }
}
