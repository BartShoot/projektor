package top.cinema.app.fetching.helios.model;

import java.util.List;
import java.util.Map;

public record ShowingsDataPayloadDto(
        Map<String, Map<String, DailyMovieScreeningsDto>> screenings, // Date -> Movie/Event Key -> Screenings
        Map<String, MovieDetailsDto> movies,
        Map<String, EventDetailsDto> events,
        PriceListContainerDto priceList
) {}
