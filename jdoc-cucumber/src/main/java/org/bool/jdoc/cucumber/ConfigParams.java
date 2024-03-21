package org.bool.jdoc.cucumber;

import static org.bool.jdoc.core.ConfigParam.*;

public class ConfigParams {

    private static final String PREFIX = "jdoc.cucumber.";

    public static final StringListConfigParam TEST_DIRS = new StringListConfigParam(PREFIX + "test-dirs");

    public static final StringListConfigParam TEST_FILES = new StringListConfigParam(PREFIX + "test-files");

    public static final StringConfigParam OUTPUT_DIR = new StringConfigParam(PREFIX + "output-dir");

}
