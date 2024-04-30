package org.bool.jdoc.gradle.cucumber;

import org.gradle.internal.impldep.org.apache.commons.io.file.Counters;
import org.gradle.internal.impldep.org.apache.commons.io.file.DeletingPathVisitor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class JdocCucumberDeleteAction {

    public void delete(Path path, String prefix, String suffix) {
        try (Stream<Path> files = Files.list(path)) {
            files.filter(p -> matches(p.getFileName().toString(), prefix, suffix))
                .forEach(this::delete);
        } catch (IOException e) {
            throw new UncheckedIOException("Error listing files: " + path, e);
        }
    }

    public void delete(Path path) {
        if (Files.exists(path)) {
            try {
                Files.walkFileTree(path, new DeletingPathVisitor(Counters.noopPathCounters()));
            } catch (IOException e) {
                throw new UncheckedIOException("Error on deletion: " + path, e);
            }
        }
    }

    private boolean matches(String fileName, String prefix, String suffix) {
        return fileName.startsWith(prefix) && fileName.endsWith(suffix);
    }
}
