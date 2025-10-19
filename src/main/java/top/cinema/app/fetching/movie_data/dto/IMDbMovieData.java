package top.cinema.app.fetching.movie_data.dto;

import top.cinema.app.entities.core.MovieGenre;

import java.util.Set;

public record IMDbMovieData(
        String id, String url, Float rating, Integer ratingCount, String posterUrl, Set<MovieGenre> genres) {}
