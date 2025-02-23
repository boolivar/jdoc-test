package org.bool.jdoc.core;

import org.bool.jdoc.core.exception.JdocException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.CommentsCollection;
import com.github.javaparser.ast.comments.JavadocComment;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JavaFileParser {

    private final JavaParser javaParser;

    private final JdocParser jdocParser;

    public JavaFileParser(String lang) {
        this(new JavaParser(new ParserConfiguration().setLanguageLevel(LanguageLevel.BLEEDING_EDGE)), new RegexParser(lang));
    }

    /**
     * Parse java file.
     * 
     * <pre><code lang="spock">
     * def "Parsing result has code from <code></code> blocks"() {
     *   given:
     *     def parser = new JavaFileParser("spock")
     *   when:
     *     def result = parser.parse(java.nio.file.Paths.get("src/main/java/org/bool/jdoc/core/JavaFileParser.java"))
     *   then:
     *     result.codeBlocks.size() == 1
     *     result.codeBlocks[0].startsWith('\n def "Parsing result has code from <code></code> blocks"() {')
     *     result.codeBlocks[0].contains('parser.parse(java.nio.file.Paths.get("src/main/java/org/bool/jdoc/core/JavaFileParser.java"))')
     *     result.codeBlocks[0].contains('infinite recursion ]8D')
     * }
     * </code></pre>
     */
    @SneakyThrows
    public SpecSource parse(Path file) {
        ParseResult<CompilationUnit> result = javaParser.parse(file);
        if (!result.isSuccessful()) {
            throw new JdocException("Error parse file " + file + ": " + result.getProblems());
        }
        return new SpecSource(result.getResult().get(),
                result.getCommentsCollection().map(this::parseComments).orElse(Collections.emptyList()));
    }

    private List<String> parseComments(CommentsCollection comments) {
        return comments.getComments().stream()
            .filter(comment -> comment instanceof JavadocComment || comment instanceof BlockComment)
            .flatMap(comment -> jdocParser.parse(comment.getContent()).stream())
            .filter(jdoc -> !jdoc.chars().allMatch(Character::isWhitespace))
            .collect(Collectors.toList());
    }
}
