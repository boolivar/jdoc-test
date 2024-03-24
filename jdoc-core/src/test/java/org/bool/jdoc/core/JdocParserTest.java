package org.bool.jdoc.core;

import org.jsoup.select.Evaluator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class JdocParserTest {

    static Stream<JdocParser> anyLangParser() {
        return Stream.of(new JsoupParser(new Evaluator.Tag("code")), new RegexParser((open, close) -> true));
    }

    static Stream<JdocParser> testLangParser() {
        return Stream.of(new JsoupParser("test"), new RegexParser("test"));
    }

    @MethodSource("anyLangParser")
    @ParameterizedTest
    void testParse(JdocParser parser) {
        assertThat(parser.parse("""
              * <pre><code>
              *  def "parse text and remove asterisks"() {
              *    given:
              *      def parser = new CodeBlockParser()
              *    when:
              *      def result = parser.parse("<code> * some code with nested<code>blocks</code></code>")
              *    then:
              *      result == [" some code with nested<code>blocks</code>"]
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

    @MethodSource("anyLangParser")
    @ParameterizedTest
    void testParseLangAttribute(JdocParser parser) {
        assertThat(parser.parse("""
               <pre><code lang="test">
                 Test Code
               </code></pre>
               """))
            .singleElement().asString()
            .isEqualToIgnoringNewLines("  Test Code");
    }

    @MethodSource("testLangParser")
    @ParameterizedTest
    void testFilterLangAttribute(JdocParser parser) {
        assertThat(parser.parse("""
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

    @MethodSource("anyLangParser")
    @ParameterizedTest
    void testEmpty(JdocParser parser) {
        Stream.of("", " ", "sometext", "// comment", "/* comment */", "/** javadoc */")
            .forEach(content -> assertThat(parser.parse(content)).describedAs("parse '%s' expected to be empty", content).isEmpty());
    }

    @MethodSource("anyLangParser")
    @ParameterizedTest
    void testNested(JdocParser parser) {
        assertThat(parser.parse("""
                <pre>
                    <code><code>one</code></code>
                    <code>two</code>
                </pre>
                <code>three</code>
                """))
            .contains("<code>one</code>", "two", "three");
    }

    @MethodSource("anyLangParser")
    @ParameterizedTest
    void testOpenTags(JdocParser parser) {
        assertThat(parser.parse("""
                <pre>
                    <code>abc<key><value><code>de</code></code>
                </pre>
                """)).contains("abc<key><value><code>de</code>");
    }
}
