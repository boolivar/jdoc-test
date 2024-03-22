package org.bool.jdoc.core;

import org.jsoup.select.Evaluator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class JdocParserTest {

    private final JdocParser parser = new JdocParser(new Evaluator.Tag("code"));

    @Test
    void testParse() {
        assertThat(parser.parse("""
              <pre><code>
                def "parse text and remove asterisks"() {
                  given:
                    def parser = new CodeBlockParser()
                  when:
                    def result = parser.parse("<code> * some code with nested<code>blocks</code></code>")
                  then:
                    result == [" some code with nested<code>blocks</code>"]
                 }
              </pre></code>
               """))
            .singleElement().asString()
            .isEqualToIgnoringWhitespace("""
                 def "parse text and remove asterisks"() {
                   given:
                     def parser = new CodeBlockParser()
                   when:
                     def result = parser.parse("<code> * some code with nested<code>blocks</code></code>")
                   then:
                     result == [" some code with nested<code>blocks</code>"]
                 }
                 """);
    }

    @ValueSource(strings = {"", " ", "sometext", "// comment", "/* comment */", "/** javadoc */"})
    @ParameterizedTest
    void testEmpty(String content) {
        assertThat(parser.parse(content))
            .isEmpty();
    }

    @Test
    void testNested() {
        assertThat(parser.parse("""
                <pre>
                    <code><code>one</code></code>
                    <code>two</code>
                </pre>
                <code>three</code>
                """))
            .contains("<code>one</code>", "two", "three");
    }
}
