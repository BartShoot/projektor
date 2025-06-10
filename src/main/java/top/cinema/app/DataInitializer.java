package top.cinema.app; // Or any other appropriate package

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.cinema.app.dao.CinemaRepository;
import top.cinema.app.dao.CityRepository;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.entities.Cinema;
import top.cinema.app.entities.City;
import top.cinema.app.entities.Movie;
import top.cinema.app.entities.Showing;
import top.cinema.app.model.CinemaChain;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final CityRepository cityRepository;
    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;
    private final ShowingRepository showingRepository;

    public DataInitializer(CityRepository cityRepository, MovieRepository movieRepository, CinemaRepository cinemaRepository, ShowingRepository showingRepository) {
        this.cityRepository = cityRepository;
        this.movieRepository = movieRepository;
        this.cinemaRepository = cinemaRepository;
        this.showingRepository = showingRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");

        if (cityRepository.count() == 0) {
            initializeCities();
        } else {
            log.info("City data already exists. Skipping initialization.");
        }
            initializeCinemasAndShowings();
        log.info("Data initialization completed.");
    }

    private void initializeCinemasAndShowings() {
        log.info("Initializing cinemas and showings...");
        List<City> cities = cityRepository.findAll();
        var movie = movieRepository.save(new Movie("test", 96));
        for (var city : cities) {
            Cinema cinema = cinemaRepository.save(
                    new Cinema("Multikino Złote Tarasy", "Złota 59", CinemaChain.MULTIKINO, city));
            city.addCinema(cinema);
            cityRepository.save(city);
            Showing showing = new Showing(cinema, movie, LocalDateTime.now());
            showingRepository.save(showing);
        }
    }

    private void initializeCities() {
        cityRepository.save(new City("Warszawa")); // Assuming City constructor takes name
        cityRepository.save(new City("Kraków"));
        cityRepository.save(new City("Łódź"));
        cityRepository.save(new City("Wrocław"));
        cityRepository.save(new City("Poznań"));
        cityRepository.save(new City("Gdańsk"));
        cityRepository.save(new City("Szczecin"));
    }

    // Example for other entities (you'll need to create these methods)
        /*
        private void initializeMovies() {
            log.info("Initializing movies...");
            // movieRepository.save(new Movie("Inception", "Christopher Nolan", 148));
            // ...
        }

        private void initializeCinemasAndShowings() {
            log.info("Initializing cinemas and showings...");
            // Fetch or create cities and movies first
            // City warsaw = cityRepository.findByName("Warszawa").orElseThrow(); // Example
            // Movie inception = movieRepository.findByTitle("Inception").orElseThrow(); // Example

            // Cinema cinema1 = new Cinema("Multikino Złote Tarasy", "Złota 59", CinemaChain.MULTIKINO, warsaw);
            // cinemaRepository.save(cinema1);

            // Showing showing1 = new Showing(cinema1, inception, LocalDateTime.now().plusHours(2), 25.00);
            // cinema1.addShowing(showing1); // If you have helper methods
            // showingRepository.save(showing1); // Or save showing directly if Cinema doesn't cascade save Showings
            // ...
        }
        */
}
