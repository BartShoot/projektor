package top.cinema.app;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.cinema.app.fetching.durable_jobs.Scheduler;
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
    private final Scheduler scheduler;

    @Autowired
    public DataInitializer(CitySaver citySaver,
                           CinemaSaver cinemaSaver,
                           MovieSaver movieSaver,
                           ShowingSaver showingSaver,
                           Scheduler scheduler) {
        this.citySaver = citySaver;
        this.cinemaSaver = cinemaSaver;
        this.movieSaver = movieSaver;
        this.showingSaver = showingSaver;
        this.scheduler = scheduler;
    }

    @Override
    @Transactional
    public void run(String... args) {
//        scheduler.createJobs();
//        citySaver.processCities();
//        cinemaSaver.processCinemas();
//        movieSaver.processMovies();
//        showingSaver.processShowings();
    }

}
