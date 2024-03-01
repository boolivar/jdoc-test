package org.bool.jdoc.spock;

import com.github.javaparser.ast.CompilationUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecSource {

    private CompilationUnit unit;

    private List<String> codeBlocks;
}
