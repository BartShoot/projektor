package top.cinema.app.dto.model;

public record CinemaSummaryDto(
    Integer id,
    String name,
    String location,
    Integer showingCounts
) {
}
