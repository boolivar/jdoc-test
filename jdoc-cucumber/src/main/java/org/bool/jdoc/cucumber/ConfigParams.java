package org.bool.jdoc.cucumber;

import static org.bool.jdoc.core.ConfigParam.*;

/**
 * jdoc-cucumber platform configuration properties.
 * 
 * <pre><code lang="gherkin">
 * Feature: read configuration properties
 *   Read junit platform configuration properties
 *   for jdoc-cucumber engine.
 *
 *   Scenario Outline: string properties
 *     Given "<key>" property set to "value"
 *     When configuration properties requested with <param> config param
 *     Then value should be "value"
 *     Examples: #</param></key>
 *       | param      | key                      |
 *       | OUTPUT_DIR | jdoc.cucumber.output-dir |
 *
 *   Scenario Outline: list properties
 *     Given "<key>" property set to "src/main/java,build/generated"
 *     When configuration properties requested with <param> config param
 *     Then value should be list of:
 *       | src/main/java   |
 *       | build/generated |
 *     Examples: #</param></key>
 *       | param      | key                      |
 *       | TEST_DIRS  | jdoc.cucumber.test-dirs  |
 *       | TEST_FILES | jdoc.cucumber.test-files |
 *
 *   Scenario Outline: empty values
 *     When configuration properties requested with <param> config param
 *     Then value should be empty
 *     Examples: #</param>
 *       | param      |
 *       | OUTPUT_DIR |
 *       | TEST_DIRS  |
 *       | TEST_FILES |
 * </code></pre>
 */
public class ConfigParams {

    private static final String PREFIX = "jdoc.cucumber.";

    public static final StringListConfigParam TEST_DIRS = new StringListConfigParam(PREFIX + "test-dirs");

    public static final StringListConfigParam TEST_FILES = new StringListConfigParam(PREFIX + "test-files");

    public static final StringConfigParam OUTPUT_DIR = new StringConfigParam(PREFIX + "output-dir");

}
