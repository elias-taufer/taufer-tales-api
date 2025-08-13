package com.taufer.tales.commentstream;

import com.taufer.tales.dto.*;
import com.taufer.tales.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Validated
public class CommentController {
    private final CommentService svc;

    @GetMapping("/review/{reviewId}")
    public List<CommentResponse> list(@PathVariable Long reviewId) {
        return svc.listByReview(reviewId);
    }

    @PostMapping
    public CommentResponse create(@RequestBody @Valid CommentCreateDto d, Authentication auth) {
        return svc.create(d, auth);
    }
}
