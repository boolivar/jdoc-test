package org.bool.jdoc.spock;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.junit.platform.engine.ConfigurationParameters;

import java.nio.file.Files;

import static org.bool.jdoc.spock.ConfigParams.*;

@AllArgsConstructor
public class CompilerConfigurationFactory {

    public CompilerConfiguration createCompilerConfig(ConfigurationParameters params) {
        CompilerConfiguration config = new CompilerConfiguration();
        config.setClasspathList(CLASSPATH.get(params));
        config.setTargetDirectory(GENERATED_CLASSES_DIR.maybeGet(params).orElseGet(this::tempDir));
        return config;
    }

    @SneakyThrows
    private String tempDir() {
        return Files.createTempDirectory("jdoc-spock-").toString();
    }
}
