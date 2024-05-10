package org.bool.jdoc.gradle.spock;

import org.bool.jdoc.gradle.JdocAction;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JdocSpockAction implements JdocAction {

    private final ClassLoader classLoader;

    private final SpecGenerator specGenerator;

    public JdocSpockAction(ClassLoader classLoader, SpecGenerator specGenerator) {
        this.classLoader = classLoader;
        this.specGenerator = specGenerator;
    }

    @Override
    public void generate(File sourceFile, String baseName, Path outputPath) {
        specGenerator.generateSpec(sourceFile.toPath(), classLoader)
            .ifPresent(spec -> writeSpec(outputPath, baseName, spec.getScript()));
    }

    @Override
    public void delete(File sourceFile, String baseName, Path outputPath) {
        Path file = resolveFile(outputPath, baseName);
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new UncheckedIOException("Error delete file: " + file, e);
        }
    }

    private void writeSpec(Path outputPath, String baseName, String content) {
        Path file = resolveFile(outputPath, baseName);
        try {
            Files.createDirectories(outputPath);
            Files.write(file, content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException("Error writing spec: " + file, e);
        }
    }

    private Path resolveFile(Path outputPath, String baseName) {
        return outputPath.resolve(baseName + "TestSpec.groovy");
    }
}
