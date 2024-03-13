package org.bool.jdoc.spock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.ConfigurationParameters;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ConfigParamsTest {

    @Mock
    private ConfigurationParameters params;

    @Mock
    private ConfigurationParameters emptyParams;

    @Test
    void testStringConfigParam() {
        given(params.get("config.param"))
                .willReturn(Optional.of("test"));

        var param = new ConfigParams.StringConfigParam("config.param");

        assertThat(param.maybeGet(params))
                .hasValue("test");
        assertThat(param.maybeGet(emptyParams))
                .isEmpty();
    }

    @Test
    void testStringListConfigParam() {
        given(params.get("list.config.param"))
                .willReturn(Optional.of("a,b"));

        var param = new ConfigParams.StringListConfigParam("list.config.param");

        assertThat(param.maybeGet(params))
                .hasValue(List.of("a", "b"));
        assertThat(param.get(params))
                .isEqualTo(List.of("a", "b"));

        assertThat(param.maybeGet(emptyParams))
                .isEmpty();
        assertThat(param.get(emptyParams))
                .isEmpty();
    }
}
