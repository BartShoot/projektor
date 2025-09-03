package top.cinema.app.fetching.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.entities.Movie;
import top.cinema.app.fetching.cinemacity.api.CinemaCityApiPort;
import top.cinema.app.fetching.helios.api.HeliosApiPort;
import top.cinema.app.fetching.multikino.api.MultikinoApiPort;

import java.util.Set;
import java.util.stream.Collectors;

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

        Set<String> normalizedTitles = movieRepository.findAll().stream()
                .map(movie -> TitleNormalizer.normalize(movie.getOriginalTitle()))
                .collect(Collectors.toSet());
        // TODO create fuzzy search

        saveHeliosMovies(normalizedTitles);
        saveCinemaCityMovies(normalizedTitles);
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

    private void saveCinemaCityMovies(Set<String> normalizedTitles) {
        var cinemaCityMovies = cinemaCityApiPort.fetchMoviesData();
        log.info("All cc movies: %s".formatted(cinemaCityMovies.body().films().size()));
        cinemaCityMovies.body().films().forEach(cinemaCityMovie -> {
            String normalizedTitle = TitleNormalizer.normalize(cinemaCityMovie.title());
            if (movieRepository.findByCinemaCityId(cinemaCityMovie.id()).isEmpty()) {
                if (!normalizedTitles.contains(normalizedTitle)) {
                    Movie movie = new Movie(cinemaCityMovie.title(), normalizedTitle,
                            cinemaCityMovie.durationMinutes());
                    movie.setCinemaCityId(cinemaCityMovie.id());
                    movieRepository.save(movie);
                    normalizedTitles.add(normalizedTitle);
                    log.info("CinemaCity movie saved: {}", normalizedTitle);
                } else {
                    Movie movie = movieRepository.findByNormalizedTitle(normalizedTitle).get();
                    movie.setCinemaCityId(cinemaCityMovie.id());
                    movieRepository.save(movie);
                    log.info("Movie showing in cinemaCity: {}", normalizedTitle);
                }
            } else {
                log.info("CinemaCity movie skipped: {}", normalizedTitle);
            }
        });
    }

    private void saveHeliosMovies(Set<String> normalizedTitles) {
        var heliosShowings = heliosApiPort.fetchShowingsData();
        log.info("All helios movies: %s".formatted(heliosShowings.data().movies().size()));
        heliosShowings.data().movies().values().forEach(heliosMovie -> {
            String normalizedTitle = TitleNormalizer.normalize(heliosMovie.title());
            if (movieRepository.findByHeliosId(heliosMovie.id()).isEmpty()) {
                if (!normalizedTitles.contains(normalizedTitle)) {
                    Movie movie = new Movie(heliosMovie.title(), normalizedTitle, heliosMovie.titleOriginal(),
                            heliosMovie.duration());
                    movie.setHeliosId(heliosMovie.id());
                    movieRepository.save(movie);
                    normalizedTitles.add(normalizedTitle);
                    log.info("Helios movie saved: {}", normalizedTitle);
                } else {
                    Movie movie = movieRepository.findByNormalizedTitle(normalizedTitle).get();
                    movie.setHeliosId(heliosMovie.id());
                    movieRepository.save(movie);
                    log.info("Movie showing in helios: {}", normalizedTitle);
                }
            } else {
                log.info("Helios movie skipped: {}", normalizedTitle);
            }
        });
    }
}

