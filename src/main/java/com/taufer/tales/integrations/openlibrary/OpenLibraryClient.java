package com.taufer.tales.integrations.openlibrary;

import com.taufer.tales.integrations.exceptions.ExternalServiceException;
import com.taufer.tales.integrations.openlibrary.dto.OpenLibraryAuthor;
import com.taufer.tales.integrations.openlibrary.dto.OpenLibraryEdition;
import com.taufer.tales.integrations.openlibrary.dto.OpenLibraryWork;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenLibraryClient {
    private static final String BASE = "https://openlibrary.org";
    private final RestTemplate rest = new RestTemplate();

    public OpenLibraryEdition getEditionByIsbn(String isbn) {
        String url = BASE + "/isbn/" + isbn + ".json";
        try {
            ResponseEntity<OpenLibraryEdition> resp = rest.getForEntity(url, OpenLibraryEdition.class);
            return resp.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            return null; // not found on Open Library
        } catch (RestClientException e) {
            throw new ExternalServiceException("Open Library request failed", e);
        }
    }

    public String getAuthorNameByKey(String authorKey) {
        // authorKey is like "/authors/OL34184A"
        String url = BASE + authorKey + ".json";
        try {
            ResponseEntity<OpenLibraryAuthor> resp = rest.getForEntity(url, OpenLibraryAuthor.class);
            OpenLibraryAuthor body = resp.getBody();
            return body != null ? body.name : null;
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (RestClientException e) {
            throw new ExternalServiceException("Open Library request failed", e);
        }
    }

    public OpenLibraryWork getWorkByKey(String workKey) {
        String url = BASE + workKey + ".json"; // workKey like "/works/OL12345W"
        try {
            ResponseEntity<OpenLibraryWork> resp = rest.getForEntity(url, OpenLibraryWork.class);
            return resp.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }
}