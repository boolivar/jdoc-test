package org.bool.jdoc.core;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;
import org.jsoup.select.Evaluator;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class JdocParser {

    private final Evaluator codeTag;

    private final Function<String, Document> parser;

    public JdocParser(String lang) {
        this(and(new Evaluator.Tag("code"), new Evaluator.AttributeWithValue("lang", lang)));
    }

    public JdocParser(Evaluator codeTag) {
        this(codeTag, content -> Parser.xmlParser().parseInput(content, ""));
    }

    /**
     * Extract {@literal <}code {@literal lang="spock">} blocks from javadocs.
     * 
     * <pre><code lang="spock">
     * def "parse text and remove asterisks"() {
     *   given:
     *     def parser = new JdocParser("spock")
     *   when:
     *     def result = parser.parse('<code lang="spock"> * some code</code>')
     *   then:
     *     result == [" some code"]
     * }
     * </code></pre>
     */
    public List<String> parse(String content) {
        return parser.apply(content).stream()
            .filter(element -> element.is(codeTag) && element.parents().stream().noneMatch(parent -> parent.is(codeTag)))
            .map(this::retrieveCodeBlock)
            .map(Entities::unescape)
            .collect(Collectors.toList());
    }

    @SneakyThrows
    private String retrieveCodeBlock(Element codeElement) {
        try (BufferedReader reader = new BufferedReader(new StringReader(codeElement.html()))) {
            return reader.lines()
                .map(line -> line.replaceFirst("^ *\\*", ""))
                .collect(Collectors.joining("\n"));
        }
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
