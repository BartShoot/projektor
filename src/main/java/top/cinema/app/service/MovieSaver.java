package top.cinema.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.entities.Movie;
import top.cinema.app.fetching.cinemacity.api.CinemaCityApiPort;
import top.cinema.app.fetching.helios.api.HeliosApiPort;
import top.cinema.app.fetching.multikino.api.MultikinoApiPort;

@Service
public class MovieSaver {

    private static final Logger log = LoggerFactory.getLogger(MovieSaver.class);

    private final MovieRepository movieRepository;
    private final HeliosApiPort heliosApiPort;
    private final CinemaCityApiPort cinemaCityApiPort;
    private final MultikinoApiPort multikinoApiPort;

    public MovieSaver(MovieRepository movieRepository, HeliosApiPort heliosApiPort, CinemaCityApiPort cinemaCityApiPort, MultikinoApiPort multikinoApiPort) {
        this.movieRepository = movieRepository;
        this.heliosApiPort = heliosApiPort;
        this.cinemaCityApiPort = cinemaCityApiPort;
        this.multikinoApiPort = multikinoApiPort;
    }

    public void processMovies() {
        log.info("Processing movies...");

        // Helios
        var heliosShowings = heliosApiPort.fetchShowingsData();
        heliosShowings.data().movies().values().forEach(heliosMovie -> {
            if (movieRepository.findByTitle(heliosMovie.title()).isEmpty()) {
                Movie movie = new Movie(heliosMovie.title(), heliosMovie.duration());
                movieRepository.save(movie);
                log.info("Saved Helios movie: {}", heliosMovie.title());
            }
        });

        // Cinema City
        var cinemaCityMovies = cinemaCityApiPort.fetchMoviesData();
        cinemaCityMovies.body().movies().forEach(cinemaCityMovie -> {
            if (movieRepository.findByTitle(cinemaCityMovie.title()).isEmpty()) {
                Movie movie = new Movie(cinemaCityMovie.title(), cinemaCityMovie.durationMinutes());
                movieRepository.save(movie);
                log.info("Saved Cinema City movie: {}", cinemaCityMovie.title());
            }
        });

        // Multikino
        var multikinoMovies = multikinoApiPort.fetchMoviesData();
        multikinoMovies.movies().forEach(multikinoMovie -> {
            if (movieRepository.findByTitle(multikinoMovie.filmTitle()).isEmpty()) {
                Movie movie = new Movie(multikinoMovie.filmTitle(), multikinoMovie.runningTime());
                movieRepository.save(movie);
                log.info("Saved Multikino movie: {}", multikinoMovie.filmTitle());
            }
        });
    }
}

