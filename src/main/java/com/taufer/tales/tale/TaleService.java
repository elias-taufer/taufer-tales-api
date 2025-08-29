package com.taufer.tales.tale;

import com.taufer.tales.common.HtmlSanitizer;
import com.taufer.tales.tale.dto.PageResponse;
import com.taufer.tales.review.ReviewRepository;
import com.taufer.tales.tale.dto.TaleCreateDto;
import com.taufer.tales.tale.dto.TaleResponse;
import com.taufer.tales.tale.dto.TaleUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaleService {

    private final TaleRepository tales;
    private final ReviewRepository reviews;
    private final HtmlSanitizer sanitizer;

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

        sanitizer.clean(d.author());
        sanitizer.clean(d.tags());
        sanitizer.clean(d.title());
        sanitizer.clean(d.coverUrl());
        sanitizer.clean(d.description());
        sanitizer.clean(d.isbn());

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

        sanitizer.clean(d.author());
        sanitizer.clean(d.tags());
        sanitizer.clean(d.title());
        sanitizer.clean(d.coverUrl());
        sanitizer.clean(d.description());
        sanitizer.clean(d.isbn());

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