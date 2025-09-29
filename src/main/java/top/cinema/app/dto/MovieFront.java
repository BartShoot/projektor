package top.cinema.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import top.cinema.app.entities.core.MovieGenre;

import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MovieFront(
        Integer id,
        String title,
        Integer durationMinutes,
        List<ShowingFront> list,
        Integer showingsCount,
        ExternalSourceData filmweb,
        ExternalSourceData imdb,
        Set<MovieGenre> genres) {

    public record ExternalSourceData(String externalId, String url, Float rating, Integer ratingCount, String posterUrl) {}

    public MovieFront(Integer id, String title, Integer durationMinutes, Integer showingsCount) {
        this(id, title, durationMinutes, null, showingsCount, null, null, null);
    }
}
