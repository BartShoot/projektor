package top.cinema.app.fetching.cinemacity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CinemaCityMoviesBodyDto(
        @JsonProperty("films")
        List<CinemaCityFilmDto> movies
) {
}
