package eu.luminis.devcon.fallacies.offers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class SpecialOffersClient {
    private static final Logger logger = LoggerFactory.getLogger(SpecialOffersClient.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final URL endpoint;
    private final int connectTimeout;
    private final int readTimeout;

    public SpecialOffersClient(URL endpoint, int connectTimeout, int readTimeout) {
        this.endpoint = endpoint;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    public Offers getOffers() throws IOException {
        long then = System.currentTimeMillis();

        HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
        connection.setConnectTimeout(connectTimeout);
        connection.setReadTimeout(readTimeout);
        connection.connect();
        logger.info("Connecting took: {}", System.currentTimeMillis() - then);

        then = System.currentTimeMillis();
        
        try {
            InputStream inputStream = connection.getInputStream();
            logger.info("Obtaining input-stream took: {}", System.currentTimeMillis() - then);

            return readToObject(inputStream);
        } catch (SocketTimeoutException exception) {
            logger.info("Response timed out after: {}", System.currentTimeMillis() - then);
            throw exception;
        } finally {
            connection.disconnect();
        }
    }

    private Offers readToObject(InputStream inputStream) throws IOException {
        long then = System.currentTimeMillis();

        Offer[] offers = objectMapper.readValue(inputStream, Offer[].class);
        logger.info("Reading offers took: {}", System.currentTimeMillis() - then);

        return new Offers(offers);
    }

}
