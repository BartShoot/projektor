package top.cinema.app.fetching.helios.model;

import java.util.List;

public record EventDetailsDto(
        int id,
        String sourceId,
        String name,
        String slug,
        Integer age, // Can be null
        int duration,
        PosterPhotoDto posterPhoto,
        List<TrailerDto> trailers,
        List<GenreDto> genres
        // Add other fields if present in your "events" structure in showings.json
) {}
