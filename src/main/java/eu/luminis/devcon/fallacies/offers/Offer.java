package eu.luminis.devcon.fallacies.offers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * DTO for holding the Offer Data.
 */
public class Offer {

    private final String id;
    private final String name;
    private final String brand;
    private final String description;
    private final BigDecimal cost;
    private final int available;
    private final String category;

    public Offer(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("brand") String brand, @JsonProperty("description") String description, @JsonProperty("cost") BigDecimal cost, @JsonProperty("available") int available, @JsonProperty("category") String category) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.cost = cost;
        this.available = available;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public int getAvailable() {
        return available;
    }

    public String getCategory() {
        return category;
    }
}
