package org.bool.jdoc.cucumber.glue;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectDirectories;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("jdoc-cucumber")
@SelectDirectories("src")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "org.bool.jdoc.cucumber.glue")
public class CucumberTestSuite {

}
