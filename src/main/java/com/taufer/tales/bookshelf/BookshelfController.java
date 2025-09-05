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

package com.taufer.tales.bookshelf;

import com.taufer.tales.bookshelf.dto.BookshelfItemResponse;
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
