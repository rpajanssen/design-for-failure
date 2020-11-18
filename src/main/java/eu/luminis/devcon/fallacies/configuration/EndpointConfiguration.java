package eu.luminis.devcon.fallacies.configuration;

import javax.annotation.Nonnull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by willem on 05/04/16.
 */
public class EndpointConfiguration {

    @Nonnull
    private String endpoint;

    @Min(value = 0)
    @Max(Integer.MAX_VALUE)
    private int connectTimeout = 0;

    @Min(value = 0)
    @Max(Integer.MAX_VALUE)
    private int readTimeout = 0;

    public String getEndpoint() {
        return endpoint;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

}
