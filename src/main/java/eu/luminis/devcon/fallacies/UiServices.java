package eu.luminis.devcon.fallacies;

import com.yammer.tenacity.core.bundle.TenacityBundleBuilder;
import eu.luminis.devcon.fallacies.configuration.UiServicesConfiguration;
import eu.luminis.devcon.fallacies.configuration.UiServicesTenacityBundleConfigurationFactory;
import eu.luminis.devcon.fallacies.offers.OffersResource;
import eu.luminis.devcon.fallacies.offers.SpecialOffersClient;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Starting point for the Ui Services.
 */
public class UiServices extends Application<UiServicesConfiguration> {

    public static void main(String[] args) throws Exception {
        new UiServices().run(args);
    }

    @Override
    public String getName() {
        return "Ui Services";
    }

    @Override
    public void initialize(Bootstrap<UiServicesConfiguration> bootstrap) {
        bootstrap.addBundle(TenacityBundleBuilder.<UiServicesConfiguration>newBuilder().configurationFactory(new UiServicesTenacityBundleConfigurationFactory()).withCircuitBreakerHealthCheck().build());
    }

    @Override
    public void run(UiServicesConfiguration configuration, Environment environment) throws Exception {
        SpecialOffersClient specialOffersClient = buildSpecialOffersClient(configuration);
        environment.jersey().register(new OffersResource(specialOffersClient));
    }

    private SpecialOffersClient buildSpecialOffersClient(UiServicesConfiguration configuration) throws MalformedURLException {
        URL endpoint = new URL(configuration.getSpecialOffersConfiguration().getEndpoint());
        int connectionTimeout = configuration.getSpecialOffersConfiguration().getConnectTimeout();
        int readTimeout = configuration.getSpecialOffersConfiguration().getReadTimeout();
        return new SpecialOffersClient(endpoint, connectionTimeout, readTimeout);
    }
}
