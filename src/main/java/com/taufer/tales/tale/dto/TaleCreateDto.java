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

package com.taufer.tales.tale.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

public record TaleCreateDto(
        @NotBlank String title,
        @NotBlank String author,
        @Pattern(regexp="\\d{10}|\\d{13}", message="ISBN must be 10 or 13 digits") String isbn,
        @Size(max=2000) String description,
        @URL String coverUrl,
        Integer publishedYear,
        @Size(max=200) String tags
) {
}