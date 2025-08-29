package com.taufer.tales.tale;

import com.taufer.tales.tale.dto.TaleResponse;

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