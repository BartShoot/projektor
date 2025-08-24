package top.cinema.app.fetching.cinemacity.model;

import java.time.LocalDateTime;

public record CinemaCityEventDto(Integer id, String filmId, Integer cinemaId, LocalDateTime eventDateTime) {

}
