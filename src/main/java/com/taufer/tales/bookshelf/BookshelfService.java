package com.taufer.tales.bookshelf;

import com.taufer.tales.bookshelf.dto.BookshelfItemResponse;
import com.taufer.tales.tale.Tale;
import com.taufer.tales.tale.TaleMapper;
import com.taufer.tales.review.ReviewRepository;
import com.taufer.tales.tale.TaleRepository;
import com.taufer.tales.user.User;
import com.taufer.tales.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookshelfService {

    private final BookshelfRepository shelf;
    private final UserRepository users;
    private final TaleRepository tales;
    private final ReviewRepository reviews;

    private Long resolveUserId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()
                || auth.getPrincipal() == null
                || "anonymousUser".equals(auth.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        return users.findByUsername(auth.getName())
                .map(User::getId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
    }

    @Transactional
    public BookshelfItemResponse setStatus(Long taleId, ReadingStatus status, Authentication auth) {
        Long userId = resolveUserId(auth);
        Tale tale = tales.findById(taleId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tale not found"));
        User user = users.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        BookshelfEntry entry = shelf.findByUser_IdAndTale_Id(userId, taleId)
                .orElse(BookshelfEntry.builder().user(user).tale(tale).build());
        entry.setStatus(status);
        entry = shelf.save(entry);

        Double avg = reviews.avgRating(taleId);
        return new BookshelfItemResponse(TaleMapper.toResponse(tale, avg), entry.getStatus());
    }

    @Transactional(readOnly = true)
    public Optional<BookshelfItemResponse> myForTale(Long taleId, Authentication auth) {
        Long userId = resolveUserId(auth);
        return shelf.findByUser_IdAndTale_Id(userId, taleId).map(e -> {
            Double avg = reviews.avgRating(taleId);
            return new BookshelfItemResponse(TaleMapper.toResponse(e.getTale(), avg), e.getStatus());
        });
    }

    @Transactional
    public void clearStatus(Long taleId, Authentication auth) {
        Long userId = resolveUserId(auth);
        shelf.findByUser_IdAndTale_Id(userId, taleId)
                .ifPresent(shelf::delete);
    }

    @Transactional(readOnly = true)
    public List<BookshelfItemResponse> list(Authentication auth, ReadingStatus status) {
        Long userId = resolveUserId(auth);
        List<BookshelfEntry> entries = status == null
                ? shelf.findByUser_Id(userId)
                : shelf.findByUser_IdAndStatus(userId, status);

        return entries.stream().map(e -> {
            Double avg = reviews.avgRating(e.getTale().getId());
            return new BookshelfItemResponse(TaleMapper.toResponse(e.getTale(), avg), e.getStatus());
        }).toList();
    }
}
