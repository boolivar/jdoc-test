package org.bool.jdoc.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(Lifecycle.PER_CLASS)
class RegexParserTest {

    private final RegexParser anyLangParser = new RegexParser();

    private final RegexParser testLangParser = new RegexParser("test");

    @Test
    void testParse() {
        assertThat(anyLangParser.parse("""
              * <pre><code>
              *   def "parse text and remove asterisks"() {
              *     given:
              *       def parser = new CodeBlockParser()
              *     when:
              *       def result = parser.parse("<code> * some code with nested<code>blocks</code></code>")
              *     then:
              *       result == [" some code with nested<code>blocks</code>"]
              *   }
              * </code></pre>
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

    @Test
    void testParseLangAttribute() {
        assertThat(anyLangParser.parse("""
               <pre><code lang="test">
                 Test Code
               </code></pre>
               """))
            .singleElement().asString()
            .isEqualToIgnoringNewLines("  Test Code");
    }

    @Test
    void testFilterLangAttribute() {
        assertThat(testLangParser.parse("""
               <pre><code lang="test">
                 Test Code
               </code></pre>
               <pre>
                 <code>
                   No lang tag
                 </code>
                 <code lang="spock">
                   Wrong lang tag
                 </code>
                 <code lang="test">
                   One more test code
                 </code>
               </pre>
               """))
            .hasSize(2)
            .anySatisfy(body -> assertThat(body).isEqualToIgnoringWhitespace("Test Code"))
            .anySatisfy(body -> assertThat(body).isEqualToIgnoringWhitespace("One more test code"))
            ;
    }

    @ValueSource(strings = {"", " ", "sometext", "// comment", "/* comment */", "/** javadoc */"})
    @ParameterizedTest
    void testEmpty(String content) {
        assertThat(anyLangParser.parse(content))
            .describedAs("anyLangParser")
            .isEmpty();
        assertThat(testLangParser.parse(content))
            .describedAs("testLangParser")
            .isEmpty();
    }

    @Test
    void testNested() {
        assertThat(anyLangParser.parse("""
                <pre>
                    <code><code>one</code></code>
                    <code>two</code>
                </pre>
                <code>three</code>
                """))
            .contains("<code>one</code>", "two", "three");
    }

    @Test
    void testOpenTags() {
        assertThat(anyLangParser.parse("""
                <pre>
                    <code>abc<key><value><code>de</code></code>
                </pre>
                """)).contains("abc<key><value><code>de</code>");
    }

    @Test
    void testSingleLine() {
        assertThat(anyLangParser.parse("<pre><code>TEST</code></pre>"))
            .singleElement().isEqualTo("TEST");
        assertThat(anyLangParser.parse("<pre><code lang=\"test\">TEST</code></pre>"))
            .singleElement().isEqualTo("TEST");
    }
}
