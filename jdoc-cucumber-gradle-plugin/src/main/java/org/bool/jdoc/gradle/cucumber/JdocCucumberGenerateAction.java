package org.bool.jdoc.gradle.cucumber;

import org.bool.jdoc.core.JavaFileParser;

import org.gradle.api.file.FileType;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.work.ChangeType;
import org.gradle.work.FileChange;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

public class JdocCucumberGenerateAction {

    private final JdocCucumberWriteSpecAction writeAction;

    private final JdocCucumberDeleteAction deleteAction;

    public JdocCucumberGenerateAction() {
        this(new JdocCucumberWriteSpecAction(), new JdocCucumberDeleteAction());
    }

    public JdocCucumberGenerateAction(JdocCucumberWriteSpecAction writeAction, JdocCucumberDeleteAction deleteAction) {
        this.writeAction = writeAction;
        this.deleteAction = deleteAction;
    }

    public void generateFeatures(Iterable<FileChange> changes, SourceDirectorySet sources, JavaFileParser parser, Path outputRoot) {
        for (FileChange change : changes) {
            if (change.getFileType() == FileType.FILE) {
                Path file = change.getFile().toPath();
                Path path = outputRoot.resolve(relativizePath(sources, file.getParent()));
                String name = baseName(Objects.toString(file.getFileName(), null));

                if (change.getChangeType() != ChangeType.ADDED) {
                    deleteAction.delete(path, name + "_", ".feature");
                }
                if (change.getChangeType() != ChangeType.REMOVED) {
                    writeAction.writeSpecs(path, name, parser.parse(file));
                }
            } else if (change.getFileType() == FileType.DIRECTORY && change.getChangeType() == ChangeType.REMOVED) {
                deleteAction.delete(outputRoot.resolve(relativizePath(sources, change.getFile().toPath())));
            }
        }
    }

    private Path relativizePath(SourceDirectorySet sources, Path path) {
        for (File dir : sources.getSrcDirs()) {
            Path srcDir = dir.toPath();
            if (path.startsWith(srcDir)) {
                return srcDir.relativize(path);
            }
        }
        throw new RuntimeException("Path " + path + " not found in " + sources);
    }

    private String baseName(String fileName) {
        int index = Objects.requireNonNull(fileName, "fileName is null").lastIndexOf('.');
        return index < 0 ? fileName : fileName.substring(0, index);
    }
}
