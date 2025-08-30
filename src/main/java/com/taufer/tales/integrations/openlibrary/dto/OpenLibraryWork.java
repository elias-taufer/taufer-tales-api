package com.taufer.tales.integrations.openlibrary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenLibraryWork {
    public JsonNode description;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WorkAuthorRef {
        public AuthorKey author;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class AuthorKey { public String key; }
    }
    public List<WorkAuthorRef> authors;
}