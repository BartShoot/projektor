package top.cinema.app.fetching.cinemacity;

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
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
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
                    }
                }
                case MOVIES_SHOWINGS -> {
                    try {
                        var cinemaCityMovies = webClient.fetchMoviesData(Integer.parseInt(job.getCinemaId()),
                                                                         LocalDate.now().plusDays(1));
                        saveCinemaCityMoviesAndShowings(cinemaCityMovies);
                        job.setLastUpdateDate(LocalDateTime.now());
                        job.setStatus(Job.Status.SUCCESS);
                    } catch (Exception e) {
                        job.setLastUpdateDate(LocalDateTime.now());
                        job.setStatus(Job.Status.FAILED);
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
                                                         .map(cinema -> cinema.addressInfo()
                                                                              .city())
                                                         .collect(
                                                                 Collectors.toSet());
        incomingAddresses.removeAll(addresses);
        if (!incomingAddresses.isEmpty()) {
            Set<City> cities = incomingAddresses.stream().map(City::new).collect(Collectors.toSet());
            cityRepository.saveAll(cities);
        }
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
                         }
                     }, () -> {throw new IllegalStateException("Cinema for missing city");});
        });
    }


    private void saveCinemaCityMoviesAndShowings(CinemaCityMoviesRootDto cinemaCityMovies) {
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
                    movieRepository.save(movie);
                    normalizedTitles.add(normalizedTitle);
                } else {
                    Movie movie = movieRepository.findByNormalizedTitle(normalizedTitle).get();
                    movie.setCinemaCityId(cinemaCityMovie.id());
                    movieRepository.save(movie);
                }
            }
        });
        cinemaCityMovies.body().events().forEach(showing -> {
            if (showingRepository.findByExternalId(showing.id().toString()).isEmpty()) {
                Optional<Movie> movie = movieRepository.findByCinemaCityId(showing.filmId());
                Optional<Cinema> cinema = cinemaRepository.findByExternalId(showing.cinemaId().toString());
                if (cinema.isEmpty() || movie.isEmpty()) {
                    throw new IllegalStateException(
                            "Showing for nonexistent movie or cinema");
                }
                showingRepository.save(new Showing(showing.id().toString(),
                                                   cinema.get(),
                                                   movie.get(),
                                                   showing.eventDateTime()));
            }
        });
    }
}
