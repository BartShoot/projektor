package top.cinema.app.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ShowingSummaryDto(
    Integer id,
    LocalDateTime showingTime,
    CinemaSummaryDto cinema,
    MovieSummaryDto movie
) {
}
