package top.cinema.app.fetching.cinemacity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CinemaCityMoviesRootDto(
        CinemaCityMoviesBodyDto body
) {
}
