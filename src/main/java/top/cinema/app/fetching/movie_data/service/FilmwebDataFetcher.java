package top.cinema.app.fetching.movie_data.service;

import top.cinema.app.fetching.movie_data.dto.FilmwebMovieData;

public interface FilmwebDataFetcher {
    FilmwebMovieData getDataForMovie(String filmwebId);
}
