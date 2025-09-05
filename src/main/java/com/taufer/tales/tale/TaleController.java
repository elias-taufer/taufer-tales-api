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

package com.taufer.tales.tale;

import com.taufer.tales.tale.dto.PageResponse;
import com.taufer.tales.bookshelf.dto.BookshelfItemResponse;
import com.taufer.tales.bookshelf.dto.SetStatusRequest;
import com.taufer.tales.tale.dto.TaleCreateDto;
import com.taufer.tales.tale.dto.TaleResponse;
import com.taufer.tales.tale.dto.TaleUpdateDto;
import com.taufer.tales.bookshelf.BookshelfService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tales")
@RequiredArgsConstructor
@Validated
public class TaleController {

    private final TaleService svc;
    private final BookshelfService bookshelfService;

    @GetMapping
    public PageResponse<TaleResponse> list(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return svc.list(q, page, size);
    }

    @GetMapping("/{id}")
    public TaleResponse get(@PathVariable Long id) {
        return svc.get(id);
    }

    @PostMapping
    public TaleResponse create(@RequestBody @Valid TaleCreateDto d) {
        return svc.create(d);
    }

    @PostMapping("/import")
    public TaleResponse importByIsbn(
            @RequestParam
            @Pattern(
                    // accepts 10/13, optional hyphens/spaces, trailing X allowed for ISBN-10
                    regexp = "^(?:(?:97[89])?\\d{9}[\\dXx]|(?:97[89][- ]?)?(?:\\d[- ]?){9}[\\dXx])$",
                    message = "ISBN must be 10 or 13 characters; X allowed for ISBN-10"
            ) String isbn) {
        return svc.importByIsbn(isbn);
    }

    @PatchMapping("/{id}")
    public TaleResponse update(@PathVariable Long id, @RequestBody @Valid TaleUpdateDto d) {
        return svc.update(id, d);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        svc.delete(id);
    }

    // Reading status (per current user)
    @PutMapping("/{id}/status")
    public BookshelfItemResponse setStatus(@PathVariable Long id,
                                           @RequestBody @Valid SetStatusRequest d,
                                           Authentication auth) {
        return bookshelfService.setStatus(id, d.status(), auth);
    }

    @DeleteMapping("/{id}/status")
    public void clearStatus(@PathVariable Long id, Authentication auth) {
        bookshelfService.clearStatus(id, auth);
    }
}