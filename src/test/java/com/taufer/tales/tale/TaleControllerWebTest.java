package com.taufer.tales.tale;

import com.taufer.tales.security.JwtAuthFilter;
import com.taufer.tales.security.JwtService;
import com.taufer.tales.tale.dto.PageResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TaleController.class)
@AutoConfigureMockMvc(addFilters = false) // don’t register security filters in MockMvc
class TaleControllerWebTest {

    @Autowired MockMvc mvc;

    @MockitoBean TaleService taleService;
    @MockitoBean com.taufer.tales.bookshelf.BookshelfService bookshelfService;

    // Security beans so the context doesn’t try to build real ones:
    @MockitoBean JwtAuthFilter jwtAuthFilter;
    @MockitoBean JwtService jwtService;

    @Test
    @WithMockUser // in case @PreAuthorize is used on controller methods
    void list_returns200_withEmptyPageResponse() throws Exception {
        when(taleService.list(any(), anyInt(), anyInt()))
                .thenReturn(PageResponse.from(Page.empty()));

        mvc.perform(get("/api/tales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
