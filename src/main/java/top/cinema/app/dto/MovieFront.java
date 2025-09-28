package top.cinema.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MovieFront(
        Integer id,
        String title,
        Integer durationMinutes,
        List<ShowingFront> list,
        Integer showingsCount,
        ExternalSourceData filmweb,
        ExternalSourceData imdb) {

    public record ExternalSourceData(String url, Float rating, Integer ratingCount, String posterUrl) {}
}
