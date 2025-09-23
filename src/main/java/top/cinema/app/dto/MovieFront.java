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
        String filmwebUrl,
        Float filmwebRating,
        Integer filmwebRatingCount,
        String filmwebPosterUrl,
        String imdbUrl,
        Float imdbRating,
        Integer imdbRatingCount,
        String imdbPosterUrl) {
    public MovieFront(Integer id, String title, Integer durationMinutes, Integer showingsCount) {
        this(id, title, durationMinutes, null, showingsCount, null, null, null, null, null, null, null, null);
    }
}
