package com.taufer.tales.reviewforge;

import com.taufer.tales.dto.*;
import com.taufer.tales.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService svc;

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> get(@PathVariable Long id) {
        return svc.read(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/tale/{taleId}")
    public List<ReviewResponse> list(@PathVariable Long taleId) {
        return svc.listByTale(taleId);
    }

    @PostMapping
    public ReviewResponse create(@RequestBody ReviewCreateDto d, Authentication auth) {
        return svc.create(d, auth);
    }

    @PatchMapping("/{id}")
    public ReviewResponse update(@PathVariable Long id, @RequestBody ReviewUpdateDto reviewUpdateDto, Authentication auth) {
        return svc.update(id, reviewUpdateDto, auth);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Authentication auth) {
        svc.delete(id, auth);
    }

    @GetMapping("/my")
    public ResponseEntity<ReviewResponse> myForTale(@RequestParam("taleId") Long taleId,
                                                    Authentication auth) {
        return svc.findMyForTale(taleId, auth)
                .map(ResponseEntity::ok)                 // 200 with the review
                .orElseGet(() -> ResponseEntity.notFound().build());  // 404 if none
    }

}
