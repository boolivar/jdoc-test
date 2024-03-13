package org.bool.jdoc.spock;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.ConfigurationParameters;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CompilerConfigurationFactoryTest {

    private final CompilerConfigurationFactory factory = new CompilerConfigurationFactory();

    @Test
    void testConfig(@Mock ConfigurationParameters params) {
        given(params.get(ConfigParams.CLASSPATH.getKey()))
                .willReturn(Optional.of("/usr/lib/jdoc-spock.jar,api.jar"));
        given(params.get(ConfigParams.GENERATED_CLASSES_DIR.getKey()))
                .willReturn(Optional.of("./temp"));

        assertThat(factory.createCompilerConfig(params))
                .returns(List.of("/usr/lib/jdoc-spock.jar", "api.jar"), CompilerConfiguration::getClasspath)
                .returns(new File("./temp"), CompilerConfiguration::getTargetDirectory)
                ;
    }
}
