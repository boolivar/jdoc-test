package org.bool.jdoc.gradle;

import java.io.File;
import java.nio.file.Path;

public interface JdocAction {

    void generate(File sourceFile, String baseName, Path outputPath);

    void delete(File sourceFile, String baseName, Path outputPath);

    void deleteDir(File sourceDir, Path outputPath);
}
