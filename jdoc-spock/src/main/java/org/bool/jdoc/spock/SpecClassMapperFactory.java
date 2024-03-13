package org.bool.jdoc.spock;

import groovy.lang.GroovyClassLoader;
import lombok.AllArgsConstructor;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.junit.platform.engine.ConfigurationParameters;

@AllArgsConstructor
public class SpecClassMapperFactory {

    private final JavaFileSpecFactory specFactory;

    private final CompilerConfigurationFactory compilerConfigurationFactory;

    public SpecClassMapperFactory() {
        this(new JavaFileSpecFactory(), new CompilerConfigurationFactory());
    }

    public SpecClassMapper createMapper(ConfigurationParameters params) {
        CompilerConfiguration config = compilerConfigurationFactory.createCompilerConfig(params);
        GroovyClassLoader classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader(), config);
        GroovyCompiler compiler = new GroovyCompiler(config, classLoader);
        return new SpecClassMapper(specFactory, compiler, classLoader);
    }
}