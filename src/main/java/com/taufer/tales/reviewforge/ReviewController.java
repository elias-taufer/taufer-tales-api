package com.taufer.tales.reviewforge;

import com.taufer.tales.dto.*;
import com.taufer.tales.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService svc;

    @GetMapping("/tale/{taleId}")
    public List<ReviewResponse> list(@PathVariable Long taleId) {
        return svc.listByTale(taleId);
    }

    @PostMapping
    public ReviewResponse create(@RequestBody ReviewCreateDto d, Authentication auth) {
        return svc.create(d, auth);
    }

    @PatchMapping("/{id}")
    public ReviewResponse update(@PathVariable Long id, @RequestBody ReviewUpdateDto d, Authentication auth) {
        return svc.update(id, d, auth);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Authentication auth) {
        svc.delete(id, auth);
    }
}
