package top.cinema.app.fetching.movie_data.service;

import top.cinema.app.fetching.movie_data.dto.IMDbMovieData;

public interface IMDbDataFetcher {
    IMDbMovieData getDataForMovie(String imdbId);
}
