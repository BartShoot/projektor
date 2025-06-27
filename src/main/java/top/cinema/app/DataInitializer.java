package top.cinema.app;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.cinema.app.dao.CinemaRepository;
import top.cinema.app.dao.CityRepository;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.service.CitySaver;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final CityRepository cityRepository;
    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;
    private final ShowingRepository showingRepository;
    private final CitySaver citySaver;

    public DataInitializer(CityRepository cityRepository, MovieRepository movieRepository, CinemaRepository cinemaRepository, ShowingRepository showingRepository, CitySaver citySaver) {
        this.cityRepository = cityRepository;
        this.movieRepository = movieRepository;
        this.cinemaRepository = cinemaRepository;
        this.showingRepository = showingRepository;
        this.citySaver = citySaver;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        citySaver.processCities();
    }

}
