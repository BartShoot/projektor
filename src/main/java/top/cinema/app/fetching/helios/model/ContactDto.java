package top.cinema.app.fetching.helios.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ContactDto(
        @JsonProperty("bookingIndividual") String bookingIndividual,
        @JsonProperty("bookingGroup") String bookingGroup
) {}
