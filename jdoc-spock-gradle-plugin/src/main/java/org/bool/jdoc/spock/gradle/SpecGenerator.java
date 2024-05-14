package org.bool.jdoc.spock.gradle;

import org.bool.jdoc.core.JavaFileParser;
import org.bool.jdoc.core.SpecSource;
import org.bool.jdoc.spock.TestSpec;
import org.bool.jdoc.spock.TestSpecFactory;

import java.nio.file.Path;
import java.util.Optional;

public class SpecGenerator {

    private final JavaFileParser parser;

    private final TestSpecFactory specFactory;

    public SpecGenerator(String lang) {
        this(new JavaFileParser(lang), new TestSpecFactory());
    }

    public SpecGenerator(JavaFileParser parser, TestSpecFactory specFactory) {
        this.parser = parser;
        this.specFactory = specFactory;
    }

    public Optional<TestSpec> generateSpec(Path javaFile, ClassLoader classLoader) {
        SpecSource specSource = parser.parse(javaFile);
        return specFactory.createTestSpec(specSource, classLoader);
    }
}
