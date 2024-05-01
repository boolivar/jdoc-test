package org.bool.jdoc.gradle.cucumber;

import org.bool.jdoc.core.SpecSource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JdocCucumberWriteSpecAction {

    public void writeSpecs(Path path, String name, SpecSource spec) {
        if (!spec.getCodeBlocks().isEmpty()) {
            writeSpecs(path, name, spec.getCodeBlocks());
        }
    }

    private void writeSpecs(Path path, String name, List<String> specs) {
        try {
            Files.createDirectories(path);
            for (int i = 0; i < specs.size(); ++i) {
                Files.write(path.resolve(name + "_" + (i + 1) + ".feature"), specs.get(i).getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error writing spec for class: " + name, e);
        }
    }
}
