package com.taufer.tales.common;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
public class HtmlSanitizer {
    private final Safelist allow = Safelist.none()
            .addTags("b","i","em","strong","u","br","p","ul","ol","li","a")
            .addAttributes("a","href")
            .addProtocols("a","href","https");

    public String clean(String input) {
        return input == null ? null : Jsoup.clean(input, allow);
    }
}