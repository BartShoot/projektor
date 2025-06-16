package top.cinema.app.fetching.helios.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ScreeningMovieItemDto(
        @JsonProperty("timeFrom") String timeFrom,
        @JsonProperty("timeTo") String timeTo,
        MovieDetailsDto movie, // Reusing MovieDetailsDto if structure is identical
        MoviePrintDto moviePrint
) {}
