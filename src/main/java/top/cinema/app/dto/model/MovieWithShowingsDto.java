package top.cinema.app.dto.model;

import top.cinema.app.dto.MovieFront;

import java.util.List;
import java.util.Set;

public record MovieWithShowingsDto(
        Integer id,
        String title,
        Integer durationMinutes,
        Integer showingsCount,
        MovieFront.ExternalSourceData filmweb,
        MovieFront.ExternalSourceData imdb,
        Set<String> genres,
        List<ShowingSummaryDto> showings
) {
}
