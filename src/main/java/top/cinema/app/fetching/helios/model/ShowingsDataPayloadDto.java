package top.cinema.app.fetching.helios.model;

import java.time.LocalDate;
import java.util.Map;

public record ShowingsDataPayloadDto(
        Map<LocalDate, Map<String, DailyMovieScreeningsDto>> screenings,
        Map<String, MovieDetailsDto> movies,
        Map<String, EventDetailsDto> events,
        PriceListContainerDto priceList
) {
}
