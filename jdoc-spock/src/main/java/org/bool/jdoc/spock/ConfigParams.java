package org.bool.jdoc.spock;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.junit.platform.engine.ConfigurationParameters;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ConfigParams {

    private static final String PREFIX = "jdoc.spock.";

    public static final StringListConfigParam CLASSPATH = new StringListConfigParam(PREFIX + "classpath");

    public static final StringConfigParam GENERATED_CLASSES_DIR = new StringConfigParam(PREFIX + "generated-classes-dir");

    protected final String key;

    public String getKey() {
        return key;
    }

    public static class StringConfigParam extends ConfigParams {

        public StringConfigParam(String key) {
            super(key);
        }

        public Optional<String> maybeGet(ConfigurationParameters config) {
            return config.get(key);
        }
    }

    public static class StringListConfigParam extends ConfigParams {

        public StringListConfigParam(String key) {
            super(key);
        }

        public List<String> get(ConfigurationParameters config) {
            return maybeGet(config).orElse(List.of());
        }

        public Optional<List<String>> maybeGet(ConfigurationParameters config) {
            return config.get(key).map(value -> Arrays.asList(StringUtils.split(value, ',')));
        }
    }
}
