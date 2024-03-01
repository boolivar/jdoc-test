package org.bool.jdoc.spock;

import org.bool.jdoc.spock.exception.SpockEngineException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.CommentsCollection;
import com.github.javaparser.ast.comments.JavadocComment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JavaFileParserTest {

    @Mock
    private JavaParser javaParser;
    
    @Mock
    private CodeBlockParser codeBlockParser;

    @InjectMocks
    private JavaFileParser parser;

    @Test
    void testParse() throws IOException {
        var path = Paths.get(".");
        var unit = new CompilationUnit();
        var comments = new CommentsCollection();
        comments.addComment(new JavadocComment("/** <code> in */"));
        given(javaParser.parse(path))
            .willReturn(new ParseResult<CompilationUnit>(unit, List.of(), comments));
        given(codeBlockParser.parse("/** <code> in */"))
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
            .isInstanceOf(SpockEngineException.class);
    }
}
