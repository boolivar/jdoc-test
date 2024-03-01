package org.bool.jdoc.spock;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;
import org.jsoup.select.Evaluator;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class CodeBlockParser {

    private final Evaluator codeTag;

    private final Function<String, Document> parser;

    public CodeBlockParser() {
        this(and(new Evaluator.Tag("code"), new Evaluator.AttributeWithValue("lang", "spock")));
    }

    public CodeBlockParser(Evaluator codeTag) {
        this(codeTag, content -> Parser.xmlParser().parseInput(content, ""));
    }

    /**
     * Extract {@literal <}code {@literal lang="spock">} blocks from javadocs.
     * 
     * <pre><code lang="spock">
     * def "parse text and remove asterisks"() {
     *   given:
     *     def parser = new CodeBlockParser()
     *   when:
     *     def result = parser.parse('<code lang="spock"> * some code</code>')
     *   then:
     *     result == [" some code"]
     * }
     * </code></pre>
     */
    @SneakyThrows
    public List<String> parse(String content) {
        return parser.apply(content).stream()
            .filter(element -> element.is(codeTag) && !hasParent(element, codeTag))
            .map(this::retrieveCodeBlock)
            .map(Entities::unescape)
            .toList();
    }

    private String retrieveCodeBlock(Element codeElement) {
        return codeElement.html().lines()
            .map(line -> line.replaceFirst("^ *\\*", ""))
            .collect(Collectors.joining("\n"));
    }

    private boolean hasParent(Element element, Evaluator evaluator) {
        while (element.hasParent()) {
            element = element.parent();
            if (element.is(evaluator)) {
                return true;
            }
        }
        return false;
    }

    private static Evaluator and(Evaluator... evaluators) {
        return new Evaluator() {
            @Override
            public boolean matches(Element root, Element element) {
                return Stream.of(evaluators).allMatch(e -> e.matches(root, element));
            }
        };
    }
}
