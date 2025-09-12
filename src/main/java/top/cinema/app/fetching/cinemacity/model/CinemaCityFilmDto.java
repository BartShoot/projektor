package top.cinema.app.fetching.cinemacity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CinemaCityFilmDto(
        String id,
        @JsonProperty("name")
        String title,
        @JsonProperty("length")
        int durationMinutes,
        int releaseYear
) {
}
