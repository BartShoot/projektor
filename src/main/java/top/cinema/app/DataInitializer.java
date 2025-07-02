package top.cinema.app;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.cinema.app.service.CinemaSaver;
import top.cinema.app.service.CitySaver;
import top.cinema.app.service.MovieSaver;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CitySaver citySaver;
    private final CinemaSaver cinemaSaver;
    private final MovieSaver movieSaver;

    @Autowired
    public DataInitializer(CitySaver citySaver, CinemaSaver cinemaSaver, MovieSaver movieSaver) {
        this.citySaver = citySaver;
        this.cinemaSaver = cinemaSaver;
        this.movieSaver = movieSaver;
    }

    @Override
    @Transactional
    public void run(String... args) {
        citySaver.processCities();
        cinemaSaver.processCinemas();
        movieSaver.processMovies();
    }

}
