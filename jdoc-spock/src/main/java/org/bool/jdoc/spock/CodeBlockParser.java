package org.bool.jdoc.spock;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;

@AllArgsConstructor
public class CodeBlockParser {

    private static final String OPEN_TAG = "<code>";

    private static final String CLOSE_TAG = "</code>";

    /**
     * Extract {@literal <}code{@literal >} blocks from javadocs.
     * 
     * <pre><code>
     * def "parse text and remove asterisks"() {
     *   when:
     *     def result = $target.parse("<code> * some code with nested<code>blocks</code></code>")
     *   then:
     *     result == " some code with nested<code>blocks</code>"
     * }
     * </code></pre>
     */
    public String parse(String content) {
        return readCode(content).lines()
            .map(line -> line.replaceFirst("^ *\\*", ""))
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.joining("\n"));
    }

    private String readCode(String content) {
        int startIndex = StringUtils.indexOf(content, OPEN_TAG) + OPEN_TAG.length();
        int opened = 1;
        int index = startIndex;
        while (index < content.length()) {
            if (content.startsWith(OPEN_TAG, index)) {
                ++opened;
            } else if (content.startsWith(CLOSE_TAG, index)) {
                if (--opened == 0) {
                    return content.substring(startIndex, index);
                }
            }
            ++index;
        }
        return "";
    }
}
