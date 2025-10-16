package top.cinema.app.fetching.movie_data.service;

import org.springframework.stereotype.Service;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.dto.MovieFront;
import top.cinema.app.entities.core.FilmwebData;
import top.cinema.app.entities.core.Genre;
import top.cinema.app.entities.core.GenreRepository;
import top.cinema.app.entities.core.IMDbData;
import top.cinema.app.entities.core.MovieGenre;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieDataUpdaterImpl implements MovieDataUpdater {
    private final FilmwebDataFetcher filmwebDataFetcher;
    private final IMDbDataFetcher imDbDataFetcher;
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    public MovieDataUpdaterImpl(
            FilmwebDataFetcher filmwebDataFetcher,
            IMDbDataFetcher imDbDataFetcher,
            MovieRepository movieRepository,
            GenreRepository genreRepository) {
        this.filmwebDataFetcher = filmwebDataFetcher;
        this.imDbDataFetcher = imDbDataFetcher;
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public MovieFront updateMovie(Integer movieId, String imdbId, String filmwebId) {
        var movie = movieRepository.findById(movieId).get();
        var movieGenreEnums = new HashSet<MovieGenre>();
        if (filmwebId != null && !filmwebId.isEmpty()) {
            var filmwebData = filmwebDataFetcher.getDataForMovie(filmwebId);
            movieGenreEnums.addAll(filmwebData.genres());
            movie.setFilmwebData(new FilmwebData(
                    filmwebData.id(),
                    filmwebData.url(),
                    filmwebData.rating(),
                    filmwebData.ratingCount(),
                    filmwebData.posterUrl()));
        }
        if (imdbId != null && !imdbId.isEmpty()) {
            var IMDbData = imDbDataFetcher.getDataForMovie(imdbId);
            movieGenreEnums.addAll(IMDbData.genres());
            movie.setImdbData(new IMDbData(
                    IMDbData.id(), IMDbData.url(), IMDbData.rating(), IMDbData.ratingCount(), IMDbData.posterUrl()));
        }

        Set<Genre> existingGenres = genreRepository.findByNameIn(movieGenreEnums);
        Set<MovieGenre> existingGenreNames =
                existingGenres.stream().map(Genre::getName).collect(Collectors.toSet());

        Set<Genre> newGenres = movieGenreEnums.stream()
                .filter(name -> !existingGenreNames.contains(name))
                .map(Genre::new)
                .collect(Collectors.toSet());

        Set<Genre> allGenres = new HashSet<>(existingGenres);
        allGenres.addAll(newGenres);

        movie.setGenres(allGenres);
        movieRepository.saveAndFlush(movie);
        return movie.toFront();
    }
}
