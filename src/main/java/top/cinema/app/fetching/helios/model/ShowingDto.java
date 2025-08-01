package top.cinema.app.fetching.helios.model;

import java.time.LocalDate;

public record ShowingDto(
        String eventId,
        LocalDate date,
        ScreeningEntryDto screeningDetails
) {
}
