package com.taufer.tales.service;

import com.taufer.tales.domain.Tale;
import com.taufer.tales.dto.*;
import com.taufer.tales.common.PageResponse;
import com.taufer.tales.mapper.TaleMapper;
import com.taufer.tales.repo.ReviewRepository;
import com.taufer.tales.repo.TaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaleService {

    private final TaleRepository tales;
    private final ReviewRepository reviews;

    public PageResponse<TaleResponse> list(String q, int page, int size) {
        PageRequest pr = PageRequest.of(page, size);
        Page<Tale> result = (q == null || q.isBlank())
                ? tales.findAll(pr)
                : tales.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(q, q, pr);
        Page<TaleResponse> mapped = result.map(t -> TaleMapper.toResponse(t, reviews.avgRating(t.getId())));
        return PageResponse.from(mapped);
    }

    public TaleResponse get(Long id) {
        Tale t = tales.findById(id).orElseThrow();
        return TaleMapper.toResponse(t, reviews.avgRating(id));
    }

    @Transactional
    public TaleResponse create(TaleCreateDto d) {
        Tale t = Tale.builder()
                .title(d.title())
                .author(d.author())
                .isbn(d.isbn())
                .description(d.description())
                .coverUrl(d.coverUrl())
                .publishedYear(d.publishedYear())
                .tags(d.tags())
                .build();
        t = tales.save(t);
        return TaleMapper.toResponse(t, 0.0d);
    }

    @Transactional
    public TaleResponse update(Long id, TaleUpdateDto d) {
        Tale t = tales.findById(id).orElseThrow();
        if (d.title() != null) t.setTitle(d.title());
        if (d.author() != null) t.setAuthor(d.author());
        if (d.isbn() != null) t.setIsbn(d.isbn());
        if (d.description() != null) t.setDescription(d.description());
        if (d.coverUrl() != null) t.setCoverUrl(d.coverUrl());
        if (d.publishedYear() != null) t.setPublishedYear(d.publishedYear());
        if (d.tags() != null) t.setTags(d.tags());
        t = tales.save(t);
        return TaleMapper.toResponse(t, reviews.avgRating(id));
    }

    @Transactional
    public void delete(Long id) {
        tales.deleteById(id);
    }
}
