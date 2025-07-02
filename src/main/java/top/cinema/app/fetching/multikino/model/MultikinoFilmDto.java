package top.cinema.app.fetching.multikino.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MultikinoFilmDto(
        String filmId,
        String filmTitle,
        int runningTime
) {
}
