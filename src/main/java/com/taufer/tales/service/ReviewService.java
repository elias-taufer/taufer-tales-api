package com.taufer.tales.service;

import com.taufer.tales.domain.*;
import com.taufer.tales.dto.*;
import com.taufer.tales.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviews;
    private final TaleRepository tales;
    private final UserRepository users;

    public List<ReviewResponse> listByTale(Long taleId) {
        return reviews.findByTale_IdOrderByCreatedAtDesc(taleId).stream().map(r -> new ReviewResponse(r.getId(), r.getTale().getId(), r.getUser().getUsername(), r.getRating(), r.getTitle(), r.getBody(), r.getCreatedAt().toString())).toList();
    }

    public ReviewResponse create(ReviewCreateDto d, Authentication auth) {
        Tale t = tales.findById(d.taleId()).orElseThrow();
        User u = users.findByUsername(auth.getName()).orElseThrow();
        Review r = reviews.save(Review.builder().tale(t).user(u).rating(d.rating()).title(d.title()).body(d.body()).createdAt(Instant.now()).build());
        return new ReviewResponse(r.getId(), t.getId(), u.getUsername(), r.getRating(), r.getTitle(), r.getBody(), r.getCreatedAt().toString());
    }

    public ReviewResponse update(Long id, ReviewUpdateDto d, Authentication auth) {
        Review r = reviews.findById(id).orElseThrow();
        if (!r.getUser().getUsername().equals(auth.getName())) throw new RuntimeException("forbidden");
        r.setRating(d.rating());
        r.setTitle(d.title());
        r.setBody(d.body());
        return new ReviewResponse(reviews.save(r).getId(), r.getTale().getId(), r.getUser().getUsername(), r.getRating(), r.getTitle(), r.getBody(), r.getCreatedAt().toString());
    }

    public void delete(Long id, Authentication auth) {
        Review r = reviews.findById(id).orElseThrow();
        if (!r.getUser().getUsername().equals(auth.getName())) throw new RuntimeException("forbidden");
        reviews.delete(r);
    }
}
