package top.cinema.app.fetching.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.entities.core.Movie;
import top.cinema.app.fetching.multikino.api.MultikinoApiPort;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieSaver {

    private static final Logger log = LoggerFactory.getLogger(MovieSaver.class);

    private final MovieRepository movieRepository;
    private final MultikinoApiPort multikinoApiPort;

    public MovieSaver(MovieRepository movieRepository,
                      MultikinoApiPort multikinoApiPort) {
        this.movieRepository = movieRepository;
        this.multikinoApiPort = multikinoApiPort;
    }

    public void processMovies() {
        log.info("Processing movies...");

        Set<String> normalizedTitles = movieRepository.findAll().stream()
                                                      .map(movie -> TitleNormalizer.normalize(movie.getOriginalTitle()))
                                                      .collect(Collectors.toSet());
        // TODO create fuzzy search

        saveMultikinoMovies(normalizedTitles);
    }

    private void saveMultikinoMovies(Set<String> normalizedTitles) {
        var multikinoMovies = multikinoApiPort.fetchMoviesData();
        log.info("All multikino movies: %s".formatted(multikinoMovies.movies().size()));
        multikinoMovies.movies().forEach(multikinoMovie -> {
            String titleToNormalize = multikinoMovie.filmTitle();
            String normalizedTitle = TitleNormalizer.normalize(titleToNormalize);
            if (movieRepository.findByMultikinoId(multikinoMovie.filmId()).isEmpty()) {
                if (!normalizedTitles.contains(normalizedTitle)) {
                    Movie movie = new Movie(multikinoMovie.filmTitle(), normalizedTitle, multikinoMovie.originalTitle(),
                                            multikinoMovie.runningTime());
                    movie.setMultikinoId(multikinoMovie.filmId());
                    movieRepository.save(movie);
                    normalizedTitles.add(normalizedTitle);
                    log.info("Multikino movie saved: {}", normalizedTitle);
                } else {
                    Movie movie = movieRepository.findByNormalizedTitle(normalizedTitle).get();
                    movie.setMultikinoId(multikinoMovie.filmId());
                    movieRepository.save(movie);
                    log.info("Movie showing in multiKino: {}", normalizedTitle);
                }
            } else {
                log.info("Multikino movie skipped: {}", normalizedTitle);
            }
        });
    }

}

