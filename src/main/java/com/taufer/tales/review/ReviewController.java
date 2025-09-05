/*
 *  Copyright 2025 Elias Taufer
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.taufer.tales.review;

import com.taufer.tales.review.dto.ReviewCreateDto;
import com.taufer.tales.review.dto.ReviewResponse;
import com.taufer.tales.review.dto.ReviewUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Validated
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
    public ReviewResponse create(@RequestBody @Valid ReviewCreateDto d, Authentication auth) {
        return svc.create(d, auth);
    }

    @PatchMapping("/{id}")
    public ReviewResponse update(@PathVariable Long id, @RequestBody @Valid ReviewUpdateDto reviewUpdateDto, Authentication auth) {
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