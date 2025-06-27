package top.cinema.app.fetching.cinemacity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CinemaCityCinemasBodyDto(
        List<CinemaCityCinemaDto> cinemas
) {
}
