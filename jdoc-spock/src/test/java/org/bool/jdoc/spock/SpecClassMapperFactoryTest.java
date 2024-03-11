package org.bool.jdoc.spock;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.ConfigurationParameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SpecClassMapperFactoryTest {

    @Mock
    private JavaFileSpecFactory specFactory;

    @Mock
    private CompilerConfigurationFactory compilerConfigurationFactory;

    @InjectMocks
    private SpecClassMapperFactory factory;

    @Test
    void testCreate(@Mock ConfigurationParameters params) {
        var config = new CompilerConfiguration();

        given(compilerConfigurationFactory.createCompilerConfig(params))
                .willReturn(config);

        assertThat(factory.createMapper(params))
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("specFactory", specFactory)
                ;
    }
}
