package com.taufer.tales.tale;

import com.taufer.tales.common.HtmlSanitizer;
import com.taufer.tales.integrations.openlibrary.OpenLibraryClient;
import com.taufer.tales.integrations.openlibrary.dto.OpenLibraryEdition;
import com.taufer.tales.integrations.openlibrary.dto.OpenLibraryWork;
import com.taufer.tales.tale.dto.PageResponse;
import com.taufer.tales.review.ReviewRepository;
import com.taufer.tales.tale.dto.TaleCreateDto;
import com.taufer.tales.tale.dto.TaleResponse;
import com.taufer.tales.tale.dto.TaleUpdateDto;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaleService {

    private final TaleRepository tales;
    private final ReviewRepository reviews;
    private final HtmlSanitizer sanitizer;
    private final OpenLibraryClient openLibrary;
    private final Validator validator;

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
    public TaleResponse importByIsbn(String rawIsbn) {
        String isbn = validateAndNormalizeIsbn(rawIsbn);

        if (tales.findByIsbn(isbn).isPresent()) {
            throw new DataIntegrityViolationException("ISBN already exists");
        }

        final OpenLibraryEdition ed;
        ed = openLibrary.getEditionByIsbn(isbn);

        if (ed == null) {
            throw new NoSuchElementException("No book found for ISBN " + isbn + " on Open Library.");
        }

        String title = safeTrim(ed.title);
        if (title == null || title.isBlank()) {
            // Treat “missing title” as not found / unusable edition
            throw new NoSuchElementException("The Open Library record for ISBN " + isbn + " has no title.");
        }

        String author = resolveAuthorNames(ed);
        if ((author == null || author.isBlank()) && ed.by_statement != null) {
            author = safeTrim(ed.by_statement);
        }

        // String description = extractDescription(ed);
        String description = pickDescription(ed);
        Integer publishedYear = extractYear(ed.publish_date);
        String coverUrl = extractCoverUrl(ed);

        try { sanitizer.clean(title); } catch (Exception ignored) {}
        try { if (author != null) sanitizer.clean(author); } catch (Exception ignored) {}
        try { if (description != null) sanitizer.clean(description); } catch (Exception ignored) {}
        try { if (coverUrl != null) sanitizer.clean(coverUrl); } catch (Exception ignored) {}

        Tale t = Tale.builder()
                .title(title)
                .author(author)
                .isbn(isbn)
                .description(description)
                .coverUrl(coverUrl)
                .publishedYear(publishedYear)
                .tags(null)
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

    /* ---------- helpers (private) ---------- */

    private String validateAndNormalizeIsbn(String raw) {
        // Inner bean solely for validation
        record IsbnParam(
                @NotBlank(message = "ISBN is required")
                @jakarta.validation.constraints.Pattern(
                        // accepts 10/13 with optional hyphens/spaces; trailing X allowed for ISBN-10
                        regexp = "^(?:(?:97[89])?\\d{9}[\\dXx]|(?:97[89][- ]?)?(?:\\d[- ]?){9}[\\dXx])$",
                        message = "ISBN must be 10 or 13 characters; X allowed for ISBN-10"
                )
                String isbn
        ) {}

        var violations = validator.validate(new IsbnParam(raw));
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return raw.replaceAll("[^0-9Xx]", "").toUpperCase(Locale.ROOT);
    }

    private static String safeTrim(String s) {
        return s == null ? null : s.trim();
    }

    private String resolveAuthorNames(OpenLibraryEdition ed) {
        // Try edition authors
        if (ed.authors != null && !ed.authors.isEmpty()) {
            List<String> names = new ArrayList<>();
            for (var ref : ed.authors) {
                if (ref != null && ref.key != null) {
                    var name = openLibrary.getAuthorNameByKey(ref.key);
                    if (name != null && !name.isBlank()) names.add(name.trim());
                }
            }
            if (!names.isEmpty())
                return String.join(", ", names);
        }

        // Fallback: by_statement
        if (ed.by_statement != null && !ed.by_statement.isBlank())
            return ed.by_statement.trim();

        // Fallback: Work authors
        if (ed.works != null && !ed.works.isEmpty() && ed.works.getFirst() != null && ed.works.getFirst().key != null) {
            var work = openLibrary.getWorkByKey(ed.works.getFirst().key);
            if (work != null && work.authors != null && !work.authors.isEmpty()) {
                List<String> names = new ArrayList<>();
                for (var wa : work.authors) {
                    if (wa != null && wa.author != null && wa.author.key != null) {
                        var name = openLibrary.getAuthorNameByKey(wa.author.key);
                        if (name != null && !name.isBlank()) names.add(name.trim());
                    }
                }
                if (!names.isEmpty())
                    return String.join(", ", names);
            }
        }

        // Last resort. Avoid NOT NULL violations
        return "Unknown";
    }

    private static Integer extractYear(String publishDate) {
        if (publishDate == null) return null;
        var m = java.util.regex.Pattern.compile("(\\d{4})").matcher(publishDate);
        if (m.find()) {
            try { return Integer.parseInt(m.group(1)); } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    private static String extractCoverUrl(OpenLibraryEdition ed) {
        if (ed.covers == null || ed.covers.isEmpty() || ed.covers.getFirst() == null) return null;
        return "https://covers.openlibrary.org/b/id/" + ed.covers.getFirst() + "-L.jpg";
    }

    private String pickDescription(OpenLibraryEdition ed) {
        String fromEdition = jsonNodeToText(ed.description);
        if (fromEdition != null && !fromEdition.isBlank()) return fromEdition;

        String fromNotes = jsonNodeToText(ed.notes);
        if (fromNotes != null && !fromNotes.isBlank()) return fromNotes;

        if (ed.works != null && !ed.works.isEmpty() && ed.works.getFirst() != null && ed.works.getFirst().key != null) {
            OpenLibraryWork work = openLibrary.getWorkByKey(ed.works.getFirst().key);
            if (work != null) {
                String fromWork = jsonNodeToText(work.description);
                if (fromWork != null && !fromWork.isBlank()) return fromWork;
            }
        }
        return null;
    }

    private static String jsonNodeToText(com.fasterxml.jackson.databind.JsonNode node) {
        if (node == null) return null;
        if (node.isTextual()) return node.asText();
        if (node.has("value") && node.get("value").isTextual()) return node.get("value").asText();
        return null;
    }
}