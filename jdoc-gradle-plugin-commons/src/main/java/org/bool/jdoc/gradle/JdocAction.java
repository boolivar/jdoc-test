package org.bool.jdoc.gradle;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public interface JdocAction {

    void generate(File sourceFile, String baseName, Path outputPath);

    void delete(File sourceFile, String baseName, Path outputPath);

    default void deleteDir(File sourceDir, Path outputPath) {
        if (Files.exists(outputPath)) {
            try {
                Files.walkFileTree(outputPath, new SimpleFileVisitor<Path>() {
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
                throw new UncheckedIOException("Error traverse: " + outputPath, e);
            }
        }
    }
}
