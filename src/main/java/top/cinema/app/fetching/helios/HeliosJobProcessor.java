package top.cinema.app.fetching.helios;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import top.cinema.app.dao.CinemaRepository;
import top.cinema.app.dao.CityRepository;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.entities.core.Cinema;
import top.cinema.app.entities.core.City;
import top.cinema.app.fetching.dao.JobRepository;
import top.cinema.app.fetching.durable_jobs.Job;
import top.cinema.app.fetching.durable_jobs.JobProcessor;
import top.cinema.app.fetching.helios.api.HeliosApiPort;
import top.cinema.app.fetching.helios.model.CinemasRootDto;
import top.cinema.app.model.CinemaChain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HeliosJobProcessor implements JobProcessor {

    private final HeliosApiPort webClient;
    private final MovieRepository movieRepository;
    private final ShowingRepository showingRepository;
    private final CinemaRepository cinemaRepository;
    private final CityRepository cityRepository;
    private final JobRepository jobRepository;

    public HeliosJobProcessor(HeliosApiPort webClient, MovieRepository movieRepository, ShowingRepository showingRepository, CinemaRepository cinemaRepository, CityRepository cityRepository, JobRepository jobRepository) {
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
                        var heliosCinemas = webClient.fetchCinemasData();
                        saveHeliosCinemasAndCities(heliosCinemas);
                        job.setLastUpdateDate(LocalDateTime.now());
                        job.setStatus(Job.Status.SUCCESS);
                    } catch (Exception e) {
                        job.setLastUpdateDate(LocalDateTime.now());
                        job.setStatus(Job.Status.FAILED);
                        job.setResults("Failed with: " + e.getMessage());
                    }
                }
                case MOVIES_SHOWINGS -> {

                }
            }
            jobRepository.save(job);
        });
    }

    private void saveHeliosCinemasAndCities(CinemasRootDto heliosCinemas) {
        var allCities = cityRepository.findAll();
        Set<String> addresses = allCities.stream().map(City::getName).collect(Collectors.toSet());
        Set<String> incomingAddresses = heliosCinemas.data()
                .stream()
                .map(cinema -> cinema.location().city())
                .collect(Collectors.toSet());
        incomingAddresses.removeAll(addresses);
        if (!incomingAddresses.isEmpty()) {
            Set<City> cities = incomingAddresses.stream().map(City::new).collect(Collectors.toSet());
            allCities = cityRepository.saveAllAndFlush(cities);
        }
        saveCinemas(heliosCinemas, allCities);
    }

    private void saveCinemas(CinemasRootDto heliosCinemas, List<City> allCities) {
        Collection<Cinema> heliosCinemasFromDb = cinemaRepository.findByCinemaChain(CinemaChain.HELIOS);
        heliosCinemas.data().forEach(heliosCinema -> {
            allCities.stream().filter(
                    city -> city.getName().equals(heliosCinema.location().city())).findFirst().ifPresentOrElse(city -> {
                if (heliosCinemasFromDb.stream().filter(
                        it -> it.getName().equals(heliosCinema.name())).findFirst().isEmpty()) {
                    Cinema cinema = new Cinema(heliosCinema.name(),
                            heliosCinema.location().street(),
                            String.valueOf(heliosCinema.id()),
                            CinemaChain.HELIOS,
                            city);
                    cinemaRepository.save(cinema);
                    jobRepository.save(Job.movieShowingsJob(cinema));
                }
            }, () -> {throw new IllegalStateException("Cinema for missing city");});
        });
    }
}
