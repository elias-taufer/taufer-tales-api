package com.taufer.tales.integrations.openlibrary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenLibraryEdition {
    public String title;
    public List<AuthorRef> authors;
    public String by_statement;
    public String publish_date;
    public List<Integer> covers;
    public JsonNode description;
    public List<WorkRef> works;
    public JsonNode notes;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AuthorRef {
        public String key;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WorkRef { public String key; }
}