package top.cinema.app.fetching.helios.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ScreeningEntryDto(
        @JsonProperty("timeFrom") String timeFrom, // Consider parsing to LocalDateTime later
        @JsonProperty("saleTimeTo") String saleTimeTo, // Consider parsing to LocalDateTime later
        String sourceId,
        String cinemaSourceId,
        List<String> settings,
        CinemaScreenInfoDto cinemaScreen,
        List<ScreeningMovieItemDto> screeningMovies, // For events/marathons
        MoviePrintDto moviePrint // For single movie screenings
) {}
