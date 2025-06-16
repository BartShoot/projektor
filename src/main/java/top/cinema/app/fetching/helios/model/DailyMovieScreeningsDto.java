package top.cinema.app.fetching.helios.model;

import java.util.List;

public record DailyMovieScreeningsDto(
        List<ScreeningEntryDto> screenings,
        List<String> flags
) {}
