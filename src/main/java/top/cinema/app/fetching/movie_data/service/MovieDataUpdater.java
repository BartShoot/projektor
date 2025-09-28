package top.cinema.app.fetching.movie_data.service;

import top.cinema.app.dto.MovieFront;

public interface MovieDataUpdater {
    MovieFront updateMovie(Integer movieId, String imdbId, String filmwebId);
}
