package top.cinema.app.fetching.movie_data.service;

import org.springframework.stereotype.Service;
import top.cinema.app.entities.core.MovieGenre;
import top.cinema.app.fetching.movie_data.api.FilmwebApiClient;
import top.cinema.app.fetching.movie_data.dto.FilmwebMovieData;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

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

        Set<MovieGenre> genres = Collections.emptySet();
        if (preview.genres() != null) {
            genres = MovieGenre.fromFilmwebGenres(
                    preview.genres().stream()
                            .map(it -> it.name().text())
                            .collect(Collectors.toList())
            );
        }

        return new FilmwebMovieData(
                filmwebId,
                preview.getHtmlUrl(filmwebId),
                rating.rate(),
                rating.count(),
                preview.getPosterUrl(),
                genres
        );
    }
}
