package com.taufer.tales.bookshelf.dto;

import com.taufer.tales.bookshelf.ReadingStatus;
import jakarta.validation.constraints.NotNull;

public record SetStatusRequest(@NotNull ReadingStatus status) {}
