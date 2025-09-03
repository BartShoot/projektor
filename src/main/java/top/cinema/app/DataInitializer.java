package top.cinema.app;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.cinema.app.fetching.service.CinemaSaver;
import top.cinema.app.fetching.service.CitySaver;
import top.cinema.app.fetching.service.MovieSaver;
import top.cinema.app.fetching.service.ShowingSaver;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CitySaver citySaver;
    private final CinemaSaver cinemaSaver;
    private final MovieSaver movieSaver;
    private final ShowingSaver showingSaver;

    @Autowired
    public DataInitializer(CitySaver citySaver, CinemaSaver cinemaSaver, MovieSaver movieSaver, ShowingSaver showingSaver) {
        this.citySaver = citySaver;
        this.cinemaSaver = cinemaSaver;
        this.movieSaver = movieSaver;
        this.showingSaver = showingSaver;
    }

    @Override
    @Transactional
    public void run(String... args) {
        citySaver.processCities();
        cinemaSaver.processCinemas();
        movieSaver.processMovies();
        showingSaver.processShowings();
    }

}
