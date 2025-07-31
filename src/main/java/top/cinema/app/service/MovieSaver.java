package top.cinema.app.service;

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

        // Helios
        var heliosShowings = heliosApiPort.fetchShowingsData();
        heliosShowings.data().movies().values().forEach(heliosMovie -> {
            String titleToNormalize = heliosMovie.titleOriginal() != null ? heliosMovie.titleOriginal() : heliosMovie.title();
            String normalizedTitle = TitleNormalizer.normalize(titleToNormalize);
            if (!normalizedTitles.contains(normalizedTitle)) {
                Movie movie = new Movie(heliosMovie.title(), normalizedTitle, heliosMovie.titleOriginal(),
                        heliosMovie.duration());
                movie.setHeliosId(heliosMovie.id());
                movieRepository.save(movie);
                normalizedTitles.add(normalizedTitle);
                log.info("Saved Helios movie: {}", heliosMovie.title());
            } else {
                Movie movie = movieRepository.findByNormalizedTitle(normalizedTitle).get();
                movie.setHeliosId(heliosMovie.id());
                movieRepository.save(movie);
            }
        });

        // Cinema City
        var cinemaCityMovies = cinemaCityApiPort.fetchMoviesData();
        cinemaCityMovies.body().movies().forEach(cinemaCityMovie -> {
            String normalizedTitle = TitleNormalizer.normalize(cinemaCityMovie.title());
            if (!normalizedTitles.contains(normalizedTitle)) {
                Movie movie = new Movie(cinemaCityMovie.title(), normalizedTitle, cinemaCityMovie.durationMinutes());
                movie.setCinemaCityId(cinemaCityMovie.id());
                movieRepository.save(movie);
                normalizedTitles.add(normalizedTitle);
                log.info("Saved Cinema City movie: {}", cinemaCityMovie.title());
            } else {
                Movie movie = movieRepository.findByNormalizedTitle(normalizedTitle).get();
                movie.setCinemaCityId(cinemaCityMovie.id());
                movieRepository.save(movie);
            }
        });

        // Multikino
        var multikinoMovies = multikinoApiPort.fetchMoviesData();
        multikinoMovies.movies().forEach(multikinoMovie -> {
            String titleToNormalize = multikinoMovie.originalTitle() != null ? multikinoMovie.originalTitle() : multikinoMovie.filmTitle();
            String normalizedTitle = TitleNormalizer.normalize(titleToNormalize);
            if (!normalizedTitles.contains(normalizedTitle)) {
                Movie movie = new Movie(multikinoMovie.filmTitle(), normalizedTitle, multikinoMovie.originalTitle(),
                        multikinoMovie.runningTime());
                movie.setMultikinoId(multikinoMovie.filmId());
                movieRepository.save(movie);
                normalizedTitles.add(normalizedTitle);
                log.info("Saved Multikino movie: {}", multikinoMovie.filmTitle());
            } else {
                Movie movie = movieRepository.findByNormalizedTitle(normalizedTitle).get();
                movie.setMultikinoId(multikinoMovie.filmId());
                movieRepository.save(movie);
            }
        });
    }
}

