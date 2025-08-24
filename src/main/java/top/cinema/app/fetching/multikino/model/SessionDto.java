package top.cinema.app.fetching.multikino.model;

import java.time.LocalDateTime;

public record SessionDto(Integer sessionId, LocalDateTime startTime) {
}
