package org.bool.jdoc.spock;

import org.bool.jdoc.spock.exception.SpockEngineException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.CommentsCollection;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class JavaFileParser {

    private final JavaParser javaParser;

    private final CodeBlockParser codeBlockParser;

    public JavaFileParser() {
        this(new JavaParser(), new CodeBlockParser());
    }

    /**
     * Parse java file.
     * 
     * <pre><code lang="spock">
     * def "Parsing result has code from <code></code> blocks"() {
     *   given:
     *     def parser = new JavaFileParser()
     *   when:
     *     def result = parser.parse(java.nio.file.Paths.get("src/main/java/org/bool/jdoc/spock/JavaFileParser.java"))
     *   then:
     *     result.codeBlocks.size() == 1
     *     result.codeBlocks[0].startsWith('\n def "Parsing result has code from <code></code> blocks"() {')
     *     result.codeBlocks[0].contains('parser.parse(java.nio.file.Paths.get("src/main/java/org/bool/jdoc/spock/JavaFileParser.java"))')
     *     result.codeBlocks[0].contains('infinite recursion ]8D')
     * }
     * </code></pre>
     */
    @SneakyThrows
    public SpecSource parse(Path file) {
        ParseResult<CompilationUnit> result = javaParser.parse(file);
        if (!result.isSuccessful()) {
            throw new SpockEngineException("Error parse file " + file + ": " + result.getProblems());
        }
        List<String> code = result.getCommentsCollection().map(CommentsCollection::getJavadocComments).orElse(Set.of()).stream()
                .flatMap(comment -> codeBlockParser.parse(comment.getContent()).stream())
                .filter(StringUtils::isNotBlank)
                .toList();
        return new SpecSource(result.getResult().orElseThrow(), code);
    }
}
