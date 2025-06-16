package top.cinema.app.fetching.helios.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record MovieDetailsDto(
        int id,
        String sourceId,
        String title,
        String titleOriginal,
        String slug,
        int duration,
        Integer age, // Can be null
        @JsonProperty("imdbRating") String imdbRating, // Can be null, might be String or Double
        List<FlagDto> flags,
        List<GenreDto> genres,
        PosterPhotoDto posterPhoto,
        List<TrailerDto> trailers
) {}
