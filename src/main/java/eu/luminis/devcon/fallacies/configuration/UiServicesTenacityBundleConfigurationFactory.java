package eu.luminis.devcon.fallacies.configuration;

import com.google.common.collect.ImmutableMap;
import com.yammer.tenacity.core.bundle.BaseTenacityBundleConfigurationFactory;
import com.yammer.tenacity.core.config.BreakerboxConfiguration;
import com.yammer.tenacity.core.config.TenacityConfiguration;
import com.yammer.tenacity.core.properties.TenacityPropertyKey;
import com.yammer.tenacity.core.properties.TenacityPropertyKeyFactory;
import eu.luminis.devcon.fallacies.offers.GetSpecialOffersCommand;

import java.util.Map;

/**
 * Created by willem on 16/03/16.
 */
public class UiServicesTenacityBundleConfigurationFactory extends BaseTenacityBundleConfigurationFactory<UiServicesConfiguration> {

    @Override
    public Map<TenacityPropertyKey, TenacityConfiguration> getTenacityConfigurations(UiServicesConfiguration configuration) {
        final ImmutableMap.Builder<TenacityPropertyKey, TenacityConfiguration> builder = ImmutableMap.builder();

        builder.put(GetSpecialOffersCommand.GetSpecialOffersKeys.GET_SPECIAL_OFFERS_KEY, configuration.getTenacityConfiguration());

        return builder.build();
    }

    @Override
    public TenacityPropertyKeyFactory getTenacityPropertyKeyFactory(UiServicesConfiguration applicationConfiguration) {
        return value -> GetSpecialOffersCommand.GetSpecialOffersKeys.GET_SPECIAL_OFFERS_KEY;
    }

    @Override
    public BreakerboxConfiguration getBreakerboxConfiguration(UiServicesConfiguration configuration) {
        return configuration.getBreakerboxConfiguration();
    }
}
