package org.bool.jdoc.gradle.cucumber;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
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
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return super.visitFile(file, attrs);
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return super.postVisitDirectory(dir, exc);
                    }
                });
            } catch (IOException e) {
                throw new UncheckedIOException("Error traverse: " + path, e);
            }
        }
    }

    private boolean matches(String fileName, String prefix, String suffix) {
        return fileName.startsWith(prefix) && fileName.endsWith(suffix);
    }
}
