package eu.luminis.devcon.fallacies.offers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import java.io.IOException;
import java.util.concurrent.*;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * A Restful Resource returning Special Offers in with different resilience styles.
 */
@Path("/offers")
public class OffersResource {

    private static final Semaphore semaphore = new Semaphore(3);
    private static final ExecutorService executor = new ThreadPoolExecutor(3, 3, 1, TimeUnit.MINUTES, new SynchronousQueue<>());

    private static final Logger logger = LoggerFactory.getLogger(OffersResource.class);
    private final SpecialOffersClient specialOffersClient;

    public OffersResource(SpecialOffersClient specialOffersClient) {
        this.specialOffersClient = specialOffersClient;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Offers getOffers() throws IOException {
        return specialOffersClient.getOffers();
    }

    @Path("/circuitbreaker")
    @GET
    @Produces(APPLICATION_JSON)
    public Offers getCircuitBreakerOffers() {
        GetSpecialOffersCommand specialOffersCommand = new GetSpecialOffersCommand(specialOffersClient);
        return specialOffersCommand.execute();
    }

    @Path("/semaphore")
    @GET
    @Produces(APPLICATION_JSON)
    public Offers getSemaphoreOffers() throws IOException {
        Offers result;
        if (semaphore.tryAcquire()) {
            logger.info("Acquired a permit on semaphore: {}", semaphore.availablePermits());
            try {
                result = specialOffersClient.getOffers();
            } finally {
                semaphore.release();
            }
        } else {
            logger.warn("No permits available on semaphore: {}", semaphore.availablePermits());
            result = Offers.EMPTY;
        }
        return result;
    }

    @Path("/threadhandover")
    @GET
    @Produces(APPLICATION_JSON)
    public Offers getThreadHandoverOffers() throws ExecutionException, InterruptedException {
        try {
            Future<Offers> future = executor.submit(specialOffersClient::getOffers);
            return future.get(1, TimeUnit.SECONDS);
        } catch (RejectedExecutionException | TimeoutException e) {
            return Offers.EMPTY;
        }
    }

    @Path("/hystrix")
    @GET
    @Produces(APPLICATION_JSON)
    public Offers getHystrixOffers() throws ExecutionException, InterruptedException {
        GetSpecialOffersCommand specialOffersCommand = new GetSpecialOffersCommand(specialOffersClient);
        Future<Offers> future = specialOffersCommand.queue();
        return future.get();
    }

    @Path("/async")
    @GET
    @Produces(APPLICATION_JSON)
    public void getAsyncOffers(@Suspended AsyncResponse asyncResponse) {
        GetSpecialOffersCommand specialOffersCommand = new GetSpecialOffersCommand(specialOffersClient);
        Observable<Offers> observable = specialOffersCommand.toObservable();
        observable.subscribe((v) -> {
            asyncResponse.resume(v);
        });
    }

}
