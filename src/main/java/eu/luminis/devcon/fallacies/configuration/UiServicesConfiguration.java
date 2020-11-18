package eu.luminis.devcon.fallacies.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.tenacity.core.config.BreakerboxConfiguration;
import com.yammer.tenacity.core.config.TenacityConfiguration;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Custom configuration for the Recommendation Application
 */
public class UiServicesConfiguration extends Configuration {

    @Valid
    @NotNull
    private TenacityConfiguration tenacityConfiguration = new TenacityConfiguration();

    @Valid
    @NotNull
    private BreakerboxConfiguration breakerboxConfiguration = new BreakerboxConfiguration();

    @Valid
    @NotNull
    private EndpointConfiguration specialOffersConfiguration;

    @JsonProperty("breakerbox")
    public BreakerboxConfiguration getBreakerboxConfiguration() {
        return breakerboxConfiguration;
    }

    @JsonProperty("hystrix")
    public TenacityConfiguration getTenacityConfiguration() {
        return tenacityConfiguration;
    }

    @JsonProperty("special-offers")
    public EndpointConfiguration getSpecialOffersConfiguration() {
        return specialOffersConfiguration;
    }

}
