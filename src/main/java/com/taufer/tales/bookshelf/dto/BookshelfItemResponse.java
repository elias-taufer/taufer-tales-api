package com.taufer.tales.bookshelf.dto;

import com.taufer.tales.bookshelf.ReadingStatus;
import com.taufer.tales.tale.dto.TaleResponse;

public record BookshelfItemResponse(TaleResponse tale, ReadingStatus status) {}
