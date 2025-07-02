package top.cinema.app.fetching.multikino.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MultikinoFilmDto(
        String filmId,
        String filmTitle,
        String originalTitle,
        int runningTime,
        List<Object> filmAttributes
) {
}

