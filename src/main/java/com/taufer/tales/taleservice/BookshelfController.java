package com.taufer.tales.taleservice;

import com.taufer.tales.domain.ReadingStatus;
import com.taufer.tales.dto.BookshelfItemResponse;
import com.taufer.tales.service.BookshelfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookshelf")
@RequiredArgsConstructor
public class BookshelfController {

    private final BookshelfService svc;

    @GetMapping
    public List<BookshelfItemResponse> list(@RequestParam(value = "status", required = false) ReadingStatus status,
                                            Authentication auth) {
        return svc.list(auth, status);
    }

    @GetMapping("/my")
    public ResponseEntity<BookshelfItemResponse> myForTale(@RequestParam("taleId") Long taleId,
                                                           Authentication auth) {
        return svc.myForTale(taleId, auth)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
