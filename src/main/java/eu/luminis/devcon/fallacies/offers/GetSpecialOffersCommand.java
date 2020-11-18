package eu.luminis.devcon.fallacies.offers;

import com.yammer.tenacity.core.TenacityCommand;
import com.yammer.tenacity.core.properties.TenacityPropertyKey;

/**
 * Hystrix Command for executing call the Special Offers service.
 */
public class GetSpecialOffersCommand extends TenacityCommand<Offers> {

    public enum GetSpecialOffersKeys implements TenacityPropertyKey {
        GET_SPECIAL_OFFERS_KEY
    }

    private final SpecialOffersClient specialOffersClient;

    public GetSpecialOffersCommand(SpecialOffersClient specialOffersClient) {
        super(GetSpecialOffersKeys.GET_SPECIAL_OFFERS_KEY);
        this.specialOffersClient = specialOffersClient;
    }

    @Override
    protected Offers run() throws Exception {
        return specialOffersClient.getOffers();
    }

    @Override
    protected Offers getFallback() {
        return Offers.EMPTY;
    }

}
