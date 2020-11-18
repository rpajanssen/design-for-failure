package eu.luminis.devcon.fallacies.offers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * DTO for holding multiple offers.
 */
public class Offers implements Iterable<Offer> {

    public static final Offers EMPTY = new Offers(new Offer[0]);

    private final List<Offer> offers;

    public Offers(Offer[] offers) {
        this.offers = Arrays.asList(offers);
    }

    @Override
    public Iterator<Offer> iterator() {
        return offers.iterator();
    }
}
