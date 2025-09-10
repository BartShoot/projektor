package top.cinema.app.fetching.cinemacity;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import top.cinema.app.dao.CinemaRepository;
import top.cinema.app.dao.CityRepository;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.entities.core.Cinema;
import top.cinema.app.entities.core.City;
import top.cinema.app.entities.core.Movie;
import top.cinema.app.entities.core.Showing;
import top.cinema.app.fetching.cinemacity.api.CinemaCityApiPort;
import top.cinema.app.fetching.cinemacity.model.CinemaCityCinemasRootDto;
import top.cinema.app.fetching.cinemacity.model.CinemaCityMoviesRootDto;
import top.cinema.app.fetching.dao.JobRepository;
import top.cinema.app.fetching.durable_jobs.Job;
import top.cinema.app.fetching.durable_jobs.JobProcessor;
import top.cinema.app.fetching.service.TitleNormalizer;
import top.cinema.app.model.CinemaChain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class CinemaCityJobProcessor implements JobProcessor {

    private final CinemaCityApiPort webClient;
    private final MovieRepository movieRepository;
    private final ShowingRepository showingRepository;
    private final CinemaRepository cinemaRepository;
    private final CityRepository cityRepository;
    private final JobRepository jobRepository;

    public CinemaCityJobProcessor(CinemaCityApiPort webClient,
                                  MovieRepository movieRepository,
                                  ShowingRepository showingRepository,
                                  CinemaRepository cinemaRepository,
                                  CityRepository cityRepository,
                                  JobRepository jobRepository) {
        this.webClient = webClient;
        this.movieRepository = movieRepository;
        this.showingRepository = showingRepository;
        this.cinemaRepository = cinemaRepository;
        this.cityRepository = cityRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    @Transactional
    public void process(Collection<Job> jobs) {
        jobs.forEach(job -> {
            switch (job.getType()) {
                case CINEMAS -> {
                    try {
                        var cinemaCityCinemas = webClient.fetchCinemasData();
                        saveCinemaCityCinemasAndCities(cinemaCityCinemas);
                        job.setLastUpdateDate(LocalDateTime.now());
                        job.setStatus(Job.Status.SUCCESS);
                    } catch (Exception e) {
                        job.setLastUpdateDate(LocalDateTime.now());
                        job.setStatus(Job.Status.FAILED);
                        job.setResults("Failed with: " + e.getMessage());
                    }
                }
                case MOVIES_SHOWINGS -> {
                    try {
                        Collection<CCResult> results = new ArrayList<>();
                        for (int i = 1; i < 6; i++) {
                            LocalDate fetchDate = LocalDate.now().plusDays(i);
                            var cinemaCityMovies = webClient.fetchMoviesData(Integer.parseInt(job.getCinemaId()),
                                    fetchDate
                            );
                            results.add(saveCinemaCityMoviesAndShowings(cinemaCityMovies));
                        }
                        job.setLastUpdateDate(LocalDateTime.now());
                        job.setStatus(Job.Status.SUCCESS);
                        job.setResults(String.format("Saved %d new movies, %d updated movies, %d new showings",
                                results.stream().mapToInt(CCResult::newMovie).sum(),
                                results.stream().mapToInt(CCResult::updatedMovie).sum(),
                                results.stream().mapToInt(CCResult::newShowing).sum()));
                    } catch (Exception e) {
                        job.setLastUpdateDate(LocalDateTime.now());
                        job.setStatus(Job.Status.FAILED);
                        job.setResults("Failed with: " + e.getMessage());
                    }
                }
                case MOVIES, SHOWINGS -> throw new UnsupportedOperationException(
                        "CinemaCity does not separate movies and showings");
            }
            jobRepository.save(job);
        });
    }

    private void saveCinemaCityCinemasAndCities(CinemaCityCinemasRootDto cinemaCityCinemas) {
        var allCities = cityRepository.findAll();
        Set<String> addresses = allCities.stream().map(City::getName).collect(Collectors.toSet());
        Set<String> incomingAddresses = cinemaCityCinemas.body()
                .cinemas()
                .stream()
                .map(cinema -> cinema.addressInfo().city())
                .collect(Collectors.toSet());
        incomingAddresses.removeAll(addresses);
        if (!incomingAddresses.isEmpty()) {
            Set<City> cities = incomingAddresses.stream().map(City::new).collect(Collectors.toSet());
            allCities = cityRepository.saveAllAndFlush(cities);
        }
        saveCinemas(cinemaCityCinemas, allCities);
    }

    private void saveCinemas(CinemaCityCinemasRootDto cinemaCityCinemas, List<City> allCities) {
        Collection<Cinema> ccCinemas = cinemaRepository.findByCinemaChain(CinemaChain.CINEMA_CITY);
        cinemaCityCinemas.body().cinemas().forEach(cinemaCityCinema -> {
            allCities.stream()
                    .filter(city -> city.getName().equals(cinemaCityCinema.addressInfo().city()))
                    .findFirst()
                    .ifPresentOrElse(city -> {
                        if (ccCinemas.stream()
                                .filter(it -> it.getName().equals(cinemaCityCinema.displayName()))
                                .findFirst()
                                .isEmpty()) {
                            Cinema cinema = new Cinema(cinemaCityCinema.displayName(),
                                    cinemaCityCinema.addressInfo().address1(),
                                    cinemaCityCinema.id(),
                                    CinemaChain.CINEMA_CITY,
                                    city);
                            cinemaRepository.save(cinema);
                            jobRepository.save(Job.movieShowingsJob(cinema));
                        }
                    }, () -> {throw new IllegalStateException("Cinema for missing city");});
        });
    }


    private CCResult saveCinemaCityMoviesAndShowings(CinemaCityMoviesRootDto cinemaCityMovies) {
        AtomicInteger newMovie = new AtomicInteger(), updatedMovie = new AtomicInteger(), newShowing = new AtomicInteger();
        Set<String> normalizedTitles = movieRepository.findAll().stream()
                .map(movie -> TitleNormalizer.normalize(movie.getTitle()))
                .collect(Collectors.toSet());
        cinemaCityMovies.body().films().forEach(cinemaCityMovie -> {
            String normalizedTitle = TitleNormalizer.normalize(cinemaCityMovie.title());
            if (movieRepository.findByCinemaCityId(cinemaCityMovie.id()).isEmpty()) {
                if (!normalizedTitles.contains(normalizedTitle)) {
                    Movie movie = new Movie(cinemaCityMovie.title(), normalizedTitle,
                            cinemaCityMovie.durationMinutes());
                    movie.setCinemaCityId(cinemaCityMovie.id());
                    movieRepository.saveAndFlush(movie);
                    newMovie.incrementAndGet();
                    normalizedTitles.add(normalizedTitle);
                } else {
                    Movie movie = movieRepository.findByNormalizedTitle(normalizedTitle).get();
                    movie.setCinemaCityId(cinemaCityMovie.id());
                    movieRepository.saveAndFlush(movie);
                    updatedMovie.incrementAndGet();
                }
            }
        });
        cinemaCityMovies.body().events().forEach(showing -> {
            if (showingRepository.findByExternalId(showing.id().toString()).isEmpty()) {
                Optional<Movie> movie = movieRepository.findByCinemaCityId(showing.filmId());
                Optional<Cinema> cinema = cinemaRepository.findByExternalId(showing.cinemaId().toString());
                if (cinema.isEmpty() || movie.isEmpty()) {
                    throw new IllegalStateException("Showing for nonexistent movie or cinema");
                }
                showingRepository.save(new Showing(showing.id().toString(),
                        cinema.get(),
                        movie.get(),
                        showing.eventDateTime()));
                newShowing.incrementAndGet();
            }
        });
        return new CCResult(newMovie.get(), updatedMovie.get(), newShowing.get());
    }
}
