package org.bool.jdoc.spock;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.utils.SourceRoot;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class CodeBlockParserTest {

    private final CodeBlockParser parser = new CodeBlockParser();

    @Test
    void testParse() {
        CompilationUnit unit = new SourceRoot(Paths.get("src/main/java/"))
            .parse(CodeBlockParser.class.getPackageName(), CodeBlockParser.class.getSimpleName() + ".java");
        Comment comment = unit.getAllComments().stream()
            .filter(Comment::isJavadocComment).findFirst().orElseThrow();
        assertThat(parser.parse(comment.getContent()))
            .isEqualToNormalizingNewlines("""
            \sdef "parse text and remove asterisks"() {
               when:
                 def result = $target.parse("<code> * some code with nested<code>blocks</code></code>")
               then:
                 result == " some code with nested<code>blocks</code>"
            \s}""");
    }
}
