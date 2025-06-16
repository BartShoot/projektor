package top.cinema.app.fetching.helios.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PriceListContainerDto(
        @JsonProperty("smartPricing") SmartPricingDetailsDto smartPricing
) {}
