package top.cinema.app.dto.model;

import java.util.List;

public record CityWithCinemasDto(
        Integer id,
        String name,
        List<CinemaSummaryDto> cinemas
) {
}
