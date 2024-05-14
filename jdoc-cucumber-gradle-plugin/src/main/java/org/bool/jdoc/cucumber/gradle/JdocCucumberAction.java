package org.bool.jdoc.cucumber.gradle;

import org.bool.jdoc.core.JavaFileParser;
import org.bool.jdoc.core.SpecSource;
import org.bool.jdoc.gradle.JdocAction;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class JdocCucumberAction implements JdocAction {

    private final JavaFileParser javaParser;

    public JdocCucumberAction(String lang) {
        this(new JavaFileParser(lang));
    }

    public JdocCucumberAction(JavaFileParser javaParser) {
        this.javaParser = javaParser;
    }

    @Override
    public void generate(File sourceFile, String baseName, Path outputPath) {
        SpecSource spec = javaParser.parse(sourceFile.toPath());
        if (!spec.getCodeBlocks().isEmpty()) {
            try {
                writeFeatures(outputPath, baseName, spec.getCodeBlocks());
            } catch (IOException e) {
                throw new UncheckedIOException("Error writing spec for " + sourceFile, e);
            }
        }
    }

    @Override
    public void delete(File sourceFile, String baseName, Path outputPath) {
        try (Stream<Path> files = Files.list(outputPath)) {
            String prefix = baseName + "_";
            files.filter(p -> matches(p.getFileName().toString(), prefix, ".feature"))
                .forEach(this::delete);
        } catch (IOException e) {
            throw new UncheckedIOException("Error listing files: " + outputPath, e);
        }
    }

    private void delete(Path file) {
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new UncheckedIOException("Error delete file: " + file, e);
        }
    }

    private void writeFeatures(Path path, String name, List<String> specs) throws IOException {
        Files.createDirectories(path);
        for (int i = 0; i < specs.size(); ++i) {
            Files.write(path.resolve(name + "_" + (i + 1) + ".feature"), specs.get(i).getBytes(StandardCharsets.UTF_8));
        }
    }

    private boolean matches(String fileName, String prefix, String suffix) {
        return fileName.startsWith(prefix) && fileName.endsWith(suffix);
    }
}
