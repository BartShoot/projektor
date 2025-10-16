package top.cinema.app.dto.model;

import top.cinema.app.model.CinemaChain;

import java.util.List;

public record CinemaWithShowingsDto(
        Integer id,
        String name,
        String location,
        CinemaChain cinemaChain,
        CitySummaryDto city,
        Integer showingCounts,
        List<ShowingSummaryDto> showings
) {
}
