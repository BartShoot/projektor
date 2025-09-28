package top.cinema.app.fetching.movie_data.dto;

public record IMDbMovieData(String id, String url, Float rating, Integer ratingCount, String posterUrl) {}
