package org.bool.jdoc.cucumber.glue;

import org.bool.jdoc.core.ConfigParam;
import org.bool.jdoc.cucumber.ConfigParams;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.platform.engine.ConfigurationParameters;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

public class ConfigParamsStepdefs {

    private final ConfigurationParameters params = mock(ConfigurationParameters.class);

    private Optional<?> actual;

    @ParameterType("\\w+")
    public ConfigParam<?> param(String name) throws IllegalAccessException {
        return (ConfigParam<?>) FieldUtils.readStaticField(ConfigParams.class, name);
    }

    @Given("{string} property set to {string}")
    public void setProperty(String key, String value) {
        given(params.get(key))
            .willReturn(Optional.of(value));
    }

    @When("configuration properties requested with {param} config param")
    public void readProperty(ConfigParam<?> param) {
        actual = param.maybeGet(params);
    }

    @Then("value should be list of:")
    public void assertValues(List<String> expected) {
        assertThat(actual)
            .get().isEqualTo(expected);
    }

    @Then("value should be {string}")
    public void assertValue(String expected) {
        assertThat(actual)
            .get().isEqualTo(expected);
    }

    @Then("value should be empty")
    public void assertEmptyValue() {
        assertThat(actual).isEmpty();
    }
}
