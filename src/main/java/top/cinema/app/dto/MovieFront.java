package top.cinema.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MovieFront(Integer id, String title, Integer durationMinutes, List<ShowingFront> list) {
    public MovieFront(Integer id, String title, Integer durationMinutes) {
        this(id, title, durationMinutes, null);
    }
}
