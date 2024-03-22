package org.bool.jdoc.cucumber;

import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FeatureWriter {

    @SneakyThrows
    public List<Path> writeFeatures(Path dir, String name, List<String> specs, Instant sourceTimestamp) {
        Files.createDirectories(dir);
        return IntStream.range(0, specs.size())
                .mapToObj(index -> write(dir.resolve(name + (index + 1) + ".feature"), specs.get(index), sourceTimestamp))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private Path write(Path file, String content, Instant sourceTimestamp) {
        if (Files.notExists(file) || Files.getLastModifiedTime(file).toInstant().isBefore(sourceTimestamp)) {
            try (BufferedWriter out = Files.newBufferedWriter(file)) {
                out.write(content);
            }
        }
        return file;
    }
}
