package com.taufer.tales.dto;

import com.taufer.tales.domain.ReadingStatus;
import jakarta.validation.constraints.NotNull;

public record SetStatusRequest(@NotNull ReadingStatus status) {}
