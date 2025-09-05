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

import com.taufer.tales.tale.dto.TaleResponse;

public final class TaleMapper {
    private TaleMapper() {}
    public static TaleResponse toResponse(Tale t, Double avgRating) {
        return new TaleResponse(
                t.getId(),
                t.getTitle(),
                t.getAuthor(),
                t.getIsbn(),
                t.getDescription(),
                t.getCoverUrl(),
                t.getPublishedYear(),
                t.getTags(),
                avgRating
        );
    }
}