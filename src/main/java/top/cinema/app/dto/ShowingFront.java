package top.cinema.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ShowingFront(Integer id, MovieFront movie, CinemaFront cinema, LocalDateTime showingTime) {
}
