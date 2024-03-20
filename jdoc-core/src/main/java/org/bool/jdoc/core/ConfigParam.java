package org.bool.jdoc.core;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.junit.platform.engine.ConfigurationParameters;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public abstract class ConfigParam<T> {

    protected final String key;

    public String getKey() {
        return key;
    }

    public abstract Optional<T> maybeGet(ConfigurationParameters parameters);

    public static class StringConfigParam extends ConfigParam<String> {

        public StringConfigParam(String key) {
            super(key);
        }

        @Override
        public Optional<String> maybeGet(ConfigurationParameters config) {
            return config.get(key);
        }
    }

    public static class StringListConfigParam extends ConfigParam<List<String>> {

        public StringListConfigParam(String key) {
            super(key);
        }

        public List<String> get(ConfigurationParameters config) {
            return maybeGet(config).orElse(Collections.emptyList());
        }

        @Override
        public Optional<List<String>> maybeGet(ConfigurationParameters config) {
            return config.get(key).map(value -> Arrays.asList(StringUtils.split(value, ',')));
        }
    }
}
