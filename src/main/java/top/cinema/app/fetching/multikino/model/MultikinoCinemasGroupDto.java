package top.cinema.app.fetching.multikino.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MultikinoCinemasGroupDto(
        String alpha,
        List<MultikinoCinemaDto> cinemas
) {
}
