package top.cinema.app;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import top.cinema.app.dao.CinemaRepository;
import top.cinema.app.dao.CityRepository;
import top.cinema.app.service.CinemaSaver;
import top.cinema.app.service.CitySaver;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final CitySaver citySaver;
    private final CinemaSaver cinemaSaver;
    private final CinemaRepository cinemaRepository;
    private final CityRepository cityRepository;

    @Autowired
    public DataInitializer(CitySaver citySaver, CinemaSaver cinemaSaver, CinemaRepository cinemaRepository, CityRepository cityRepository) {
        this.citySaver = citySaver;
        this.cinemaSaver = cinemaSaver;
        this.cinemaRepository = cinemaRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        citySaver.processCities();
        cinemaSaver.processCinemas();
    }

}
