package top.cinema.app.fetching.movie_data.service;

import org.springframework.stereotype.Service;
import top.cinema.app.fetching.movie_data.api.FilmwebApiClient;
import top.cinema.app.fetching.movie_data.dto.FilmwebMovieData;

@Service
public class FilmwebDataFetcherImpl implements FilmwebDataFetcher {
    private final FilmwebApiClient client;

    public FilmwebDataFetcherImpl(FilmwebApiClient client) {
        this.client = client;
    }

    @Override
    public FilmwebMovieData getDataForMovie(String filmwebId) {
        var rating = client.fetchRating(filmwebId).getBody();
        var preview = client.fetchPreview(filmwebId).getBody();
        return new FilmwebMovieData(
                filmwebId, preview.getHtmlUrl(filmwebId), rating.rate(), rating.count(), preview.getPosterUrl());
    }
}
