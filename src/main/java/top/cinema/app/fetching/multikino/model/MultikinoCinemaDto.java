package top.cinema.app.fetching.multikino.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MultikinoCinemaDto(
        String cinemaId,
        String cinemaName
) {
}
