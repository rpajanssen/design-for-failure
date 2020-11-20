package eu.luminis.devcon.fallacies.configuration;

import com.google.common.collect.ImmutableMap;
import com.yammer.tenacity.core.bundle.BaseTenacityBundleConfigurationFactory;
import com.yammer.tenacity.core.config.BreakerboxConfiguration;
import com.yammer.tenacity.core.config.TenacityConfiguration;
import com.yammer.tenacity.core.properties.TenacityPropertyKey;
import com.yammer.tenacity.core.properties.TenacityPropertyKeyFactory;
import eu.luminis.devcon.fallacies.offers.GetSpecialOffersCommand;

import java.util.Map;

public class UiServicesTenacityBundleConfigurationFactory extends BaseTenacityBundleConfigurationFactory<UiServicesConfiguration> {

    @Override
    public Map<TenacityPropertyKey, TenacityConfiguration> getTenacityConfigurations(UiServicesConfiguration configuration) {
        final ImmutableMap.Builder<TenacityPropertyKey, TenacityConfiguration> builder = ImmutableMap.builder();

        builder.put(GetSpecialOffersCommand.GetSpecialOffersKeys.GET_SPECIAL_OFFERS, configuration.getTenacityConfiguration());

        return builder.build();
    }

    @Override
    public TenacityPropertyKeyFactory getTenacityPropertyKeyFactory(UiServicesConfiguration applicationConfiguration) {
        return value -> GetSpecialOffersCommand.GetSpecialOffersKeys.GET_SPECIAL_OFFERS;
    }

    @Override
    public BreakerboxConfiguration getBreakerboxConfiguration(UiServicesConfiguration configuration) {
        return configuration.getBreakerboxConfiguration();
    }
}