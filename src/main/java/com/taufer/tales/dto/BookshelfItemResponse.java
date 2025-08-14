package com.taufer.tales.dto;

import com.taufer.tales.domain.ReadingStatus;

public record BookshelfItemResponse(TaleResponse tale, ReadingStatus status) {}
