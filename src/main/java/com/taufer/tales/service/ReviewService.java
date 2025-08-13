package com.taufer.tales.service;

import com.taufer.tales.domain.Review;
import com.taufer.tales.domain.Tale;
import com.taufer.tales.domain.User;
import com.taufer.tales.dto.*;
import com.taufer.tales.mapper.ReviewMapper;
import com.taufer.tales.repo.ReviewRepository;
import com.taufer.tales.repo.TaleRepository;
import com.taufer.tales.repo.UserRepository;
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

    public List<ReviewResponse> listByTale(Long taleId) {
        return reviews.findByTale_IdOrderByCreatedAtDesc(taleId).stream()
                .map(ReviewMapper::toResponse)
                .toList();
    }

    @Transactional
    public ReviewResponse create(ReviewCreateDto d, Authentication auth) {
        Tale tale = tales.findById(d.taleId()).orElseThrow();
        User user = users.findByUsername(auth.getName()).orElseThrow();
        Review r = Review.builder()
                .tale(tale)
                .user(user)
                .rating(d.rating())
                .title(d.title())
                .body(d.body())
                .build();
        r = reviews.save(r);
        return ReviewMapper.toResponse(r);
    }

    public Optional<ReviewResponse> read(Long reviewId) {
        return reviews.findById(reviewId).map(ReviewMapper::toResponse);
    }

    @Transactional
    public ReviewResponse update(Long id, ReviewUpdateDto d, Authentication auth) {
        Review r = reviews.findById(id).orElseThrow();
        String username = auth.getName();
        if (!r.getUser().getUsername().equals(username)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Forbidden");
        }
        r.setRating(d.rating());
        r.setTitle(d.title());
        r.setBody(d.body());
        r = reviews.save(r);
        return ReviewMapper.toResponse(r);
    }

    @Transactional
    public void delete(Long id, Authentication auth) {
        Review r = reviews.findById(id).orElseThrow();
        String username = auth.getName();
        if (!r.getUser().getUsername().equals(username)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Forbidden");
        }
        reviews.delete(r);
    }

    public Optional<ReviewResponse> findMyForTale(Long taleId, Authentication auth) {
        Long userId = users.findByUsername(auth.getName()).orElseThrow().getId();
        return reviews.findByUserIdAndTaleId(userId, taleId).map(ReviewMapper::toResponse);
    }
}
