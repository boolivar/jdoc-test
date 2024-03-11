package org.bool.jdoc.spock;

import org.junit.platform.engine.ConfigurationParameters;

public class SpecClassMapperFactory {

    public SpecClassMapper create(ConfigurationParameters params) {
        return new SpecClassMapper();
    }
}
