package org.bool.jdoc.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.DiscoveryFilter;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.EngineDiscoveryListener;
import org.junit.platform.engine.discovery.ClassNameFilter;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.FileSelector;
import org.junit.platform.engine.discovery.PackageNameFilter;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.mock;

@ExtendWith(MockitoExtension.class)
class DiscoveryRequestTest {

    @Mock
    private ConfigurationParameters params;

    static List<DiscoveryRequest.DiscoveryRequestBuilder> testFieldsRequired() {
        return List.of(
            DiscoveryRequest.builder(),
            DiscoveryRequest.builder().selectors(List.of()),
            DiscoveryRequest.builder().params(mock(ConfigurationParameters.class))
        );
    }

    @MethodSource
    @ParameterizedTest
    void testFieldsRequired(DiscoveryRequest.DiscoveryRequestBuilder builder) {
        assertThatThrownBy(builder::build)
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testEmptyRequest() {
        var request = DiscoveryRequest.builder().params(params).selectors(List.of()).build();

        assertThat(request.getConfigurationParameters())
                .isSameAs(params);
        assertThat(request.getSelectorsByType(DiscoverySelector.class))
                .isEmpty();
        assertThat(request.getFiltersByType(DiscoveryFilter.class))
                .isEmpty();
        assertThat(request.getDiscoveryListener())
                .isSameAs(EngineDiscoveryListener.NOOP);
    }

    @Test
    void testSelectors(@Mock FileSelector selector) {
        var request = DiscoveryRequest.builder().params(params).selectors(List.of(selector)).build();

        assertThat(request.getSelectorsByType(DiscoverySelector.class))
                .containsOnly(selector);
        assertThat(request.getSelectorsByType(FileSelector.class))
                .containsOnly(selector);
        assertThat(request.getSelectorsByType(ClassSelector.class))
                .isEmpty();
    }

    @Test
    void testFilters(@Mock ClassNameFilter filter) {
        var request = DiscoveryRequest.builder().params(params).selectors(List.of()).filters(List.of(filter)).build();

        assertThat(request.getFiltersByType(DiscoveryFilter.class))
                .containsOnly(filter);
        assertThat(request.getFiltersByType(ClassNameFilter.class))
                .containsOnly(filter);
        assertThat(request.getFiltersByType(PackageNameFilter.class))
                .isEmpty();
    }
}
