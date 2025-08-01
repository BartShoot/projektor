package top.cinema.app.fetching.helios.model;

import java.time.LocalDateTime;
import java.util.List;

public record ScreeningEntryDto(
        LocalDateTime timeFrom,
        LocalDateTime saleTimeTo,
        String sourceId,
        String cinemaSourceId,
        List<String> settings,
        CinemaScreenInfoDto cinemaScreen,
        List<ScreeningMovieItemDto> screeningMovies,
        MoviePrintDto moviePrint
) {
}
