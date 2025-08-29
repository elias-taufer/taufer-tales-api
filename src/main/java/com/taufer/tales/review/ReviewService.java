package com.taufer.tales.review;

import com.taufer.tales.common.HtmlSanitizer;
import com.taufer.tales.tale.Tale;
import com.taufer.tales.review.dto.ReviewCreateDto;
import com.taufer.tales.review.dto.ReviewResponse;
import com.taufer.tales.review.dto.ReviewUpdateDto;
import com.taufer.tales.user.User;
import com.taufer.tales.tale.TaleRepository;
import com.taufer.tales.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviews;
    private final TaleRepository tales;
    private final UserRepository users;
    private final HtmlSanitizer sanitizer;

    public List<ReviewResponse> listByTale(Long taleId) {
        return reviews.findByTale_IdOrderByCreatedAtDesc(taleId).stream()
                .map(ReviewMapper::toResponse)
                .toList();
    }

    @Transactional
    public ReviewResponse create(ReviewCreateDto d, Authentication auth) {
        Long userId = resolveUserId(auth);
        Tale tale = tales.findById(d.taleId()).orElseThrow();
        User user = users.findById(userId).orElseThrow();

        sanitizer.clean(d.body());
        sanitizer.clean(d.title());

        Review r = Review.builder()
                .tale(tale).user(user)
                .rating(d.rating()).title(d.title()).body(d.body())
                .build();
        r = reviews.save(r);
        return ReviewMapper.toResponse(r);
    }

    public Optional<ReviewResponse> read(Long reviewId) {
        return reviews.findById(reviewId).map(ReviewMapper::toResponse);
    }

    @Transactional
    public ReviewResponse update(Long id, ReviewUpdateDto d, Authentication auth) {
        resolveUserId(auth);
        Review r = reviews.findById(id).orElseThrow();
        String username = auth.getName();
        if (!r.getUser().getUsername().equals(username)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN, "Forbidden");
        }

        sanitizer.clean(d.body());
        sanitizer.clean(d.title());

        r.setRating(d.rating());
        r.setTitle(d.title());
        r.setBody(d.body());
        r = reviews.save(r);
        return ReviewMapper.toResponse(r);
    }

    @Transactional
    public void delete(Long id, Authentication auth) {
        resolveUserId(auth);
        Review r = reviews.findById(id).orElseThrow();
        String username = auth.getName();
        if (!r.getUser().getUsername().equals(username)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN, "Forbidden");
        }
        reviews.delete(r);
    }

    public Optional<ReviewResponse> findMyForTale(Long taleId, Authentication auth) {
        Long userId = resolveUserId(auth);
        return reviews.findByUserIdAndTaleId(userId, taleId).map(ReviewMapper::toResponse);
    }

    private Long resolveUserId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()
                || auth.getPrincipal() == null
                || "anonymousUser".equals(auth.getPrincipal())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        return users.findByUsername(auth.getName())
                .map(User::getId)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.UNAUTHORIZED, "Unauthorized"));
    }
}