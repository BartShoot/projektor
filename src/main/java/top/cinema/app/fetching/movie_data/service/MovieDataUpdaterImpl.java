package top.cinema.app.fetching.movie_data.service;

import org.springframework.stereotype.Service;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.dto.MovieFront;
import top.cinema.app.entities.core.FilmwebData;
import top.cinema.app.entities.core.IMDbData;

@Service
public class MovieDataUpdaterImpl implements MovieDataUpdater {
    private final FilmwebDataFetcher filmwebDataFetcher;
    private final IMDbDataFetcher imDbDataFetcher;
    private final MovieRepository movieRepository;

    public MovieDataUpdaterImpl(
            FilmwebDataFetcher filmwebDataFetcher, IMDbDataFetcher imDbDataFetcher, MovieRepository movieRepository) {
        this.filmwebDataFetcher = filmwebDataFetcher;
        this.imDbDataFetcher = imDbDataFetcher;
        this.movieRepository = movieRepository;
    }

    @Override
    public MovieFront updateMovie(Integer movieId, String imdbId, String filmwebId) {
        var movie = movieRepository.findById(movieId).get();
        if (filmwebId != null) {
            var filmwebData = filmwebDataFetcher.getDataForMovie(filmwebId);
            movie.setFilmwebData(new FilmwebData(
                    filmwebData.id(),
                    filmwebData.url(),
                    filmwebData.rating(),
                    filmwebData.ratingCount(),
                    filmwebData.posterUrl()));
        }
        if (imdbId != null) {
            var IMDbData = imDbDataFetcher.getDataForMovie(imdbId);
            movie.setImdbData(new IMDbData(
                    IMDbData.id(), IMDbData.url(), IMDbData.rating(), IMDbData.ratingCount(), IMDbData.posterUrl()));
        }
        movieRepository.saveAndFlush(movie);
        return movie.toFront();
    }
}
