package com.taufer.tales.common;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class HtmlSanitizerTest {

    @Test
    void clean_handlesNullAndStripsScripts() {
        HtmlSanitizer s = new HtmlSanitizer();
        assertThat(s.clean(null)).isNull();
        assertThat(s.clean("<b>ok</b><script>alert(1)</script>")).isEqualTo("<b>ok</b>");
    }

    @Test
    void clean_keepsSafeLinksOnly() {
        HtmlSanitizer s = new HtmlSanitizer();
        String out = s.clean("<a href=\"javascript:alert(1)\">bad</a><a href=\"https://ok\">ok</a>");
        assertThat(out).doesNotContain("javascript:").contains("<a href=\"https://ok\">ok</a>");
    }
}
