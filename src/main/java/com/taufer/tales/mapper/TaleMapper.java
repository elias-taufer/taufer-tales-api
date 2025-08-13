package com.taufer.tales.mapper;

import com.taufer.tales.domain.Tale;
import com.taufer.tales.dto.TaleResponse;

public final class TaleMapper {
    private TaleMapper() {}
    public static TaleResponse toResponse(Tale t, Double avgRating) {
        return new TaleResponse(
                t.getId(),
                t.getTitle(),
                t.getAuthor(),
                t.getIsbn(),
                t.getDescription(),
                t.getCoverUrl(),
                t.getPublishedYear(),
                t.getTags(),
                avgRating
        );
    }
}
