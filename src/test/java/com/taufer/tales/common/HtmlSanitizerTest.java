/*
 *  Copyright 2025 Elias Taufer
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

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
