package org.bool.jdoc.core;

import org.bool.jdoc.core.exception.JdocException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.CommentsCollection;
import com.github.javaparser.ast.comments.JavadocComment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JavaFileParserTest {

    @Mock
    private JavaParser javaParser;

    @Mock
    private JdocParser jdocParser;

    @InjectMocks
    private JavaFileParser parser;

    static Stream<Comment> testParse() {
        return Stream.of(new JavadocComment("/** javadoc code in */"), new BlockComment("/* block code in *"));
    }

    @MethodSource
    @ParameterizedTest
    void testParse(Comment comment) throws IOException {
        var path = Paths.get(".");
        var unit = new CompilationUnit();
        var comments = new CommentsCollection();
        comments.addComment(comment);
        given(javaParser.parse(path))
            .willReturn(new ParseResult<CompilationUnit>(unit, List.of(), comments));
        given(jdocParser.parse(comment.getContent()))
            .willReturn(List.of("code", "out"));

        assertThat(parser.parse(path))
            .returns(unit, SpecSource::getUnit)
            .returns(List.of("code", "out"), SpecSource::getCodeBlocks)
            ;
    }

    @Test
    void testException() throws IOException {
        var path = Paths.get(".");
        given(javaParser.parse(path))
            .willReturn(new ParseResult<CompilationUnit>(null, List.of(), new CommentsCollection()));
        assertThatThrownBy(() -> parser.parse(path))
            .isInstanceOf(JdocException.class);
    }
}
