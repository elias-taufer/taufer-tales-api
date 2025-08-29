package com.taufer.tales.tale;

import com.taufer.tales.tale.dto.PageResponse;
import com.taufer.tales.bookshelf.dto.BookshelfItemResponse;
import com.taufer.tales.bookshelf.dto.SetStatusRequest;
import com.taufer.tales.tale.dto.TaleCreateDto;
import com.taufer.tales.tale.dto.TaleResponse;
import com.taufer.tales.tale.dto.TaleUpdateDto;
import com.taufer.tales.bookshelf.BookshelfService;
import jakarta.validation.Valid;
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