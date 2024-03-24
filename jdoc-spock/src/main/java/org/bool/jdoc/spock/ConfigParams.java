package org.bool.jdoc.spock;

import static org.bool.jdoc.core.ConfigParam.*;

public final class ConfigParams {

    private static final String PREFIX = "jdoc.spock.";

    public static final StringListConfigParam CLASSPATH = new StringListConfigParam(PREFIX + "classpath");

    public static final StringConfigParam GENERATED_CLASSES_DIR = new StringConfigParam(PREFIX + "generated-classes-dir");

    public static final StringListConfigParam TEST_DIRS = new StringListConfigParam(PREFIX + "test-dirs");

    public static final StringListConfigParam TEST_FILES = new StringListConfigParam(PREFIX + "test-files");

    private ConfigParams() {
    }
}
