package top.cinema.app.fetching.durable_jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.cinema.app.dao.CinemaRepository;
import top.cinema.app.entities.durable_jobs.Job;
import top.cinema.app.fetching.dao.JobRepository;
import top.cinema.app.model.CinemaChain;

@Service
public class Scheduler {
    private final JobRepository jobRepository;
    private final CinemaRepository cinemaRepository;

    public Scheduler(JobRepository jobRepository, CinemaRepository cinemaRepository) {
        this.jobRepository = jobRepository;
        this.cinemaRepository = cinemaRepository;
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void processJobs() {
        System.out.println("Processing jobs...");
        // maybe foreach cinema chain separately
        // var jobs = jobRepository.getOldestPending
        // switch (job.cinemaChain) {
        // switch (job.resource.getFirst().type
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void createJobs() {
        createCinemaCityJobs();
        createHeliosJobs();
        createMultikinoJobs();
    }

    private void createMultikinoJobs() {
        // multikino requires to get access to api-key or session by opening their page
        // one job to fetch all cinemas - once a week or less often

        // foreach cinema fetch movies once a day or less often ~41 requests

        //foreach cinema and foreach movie fetch showings once a day ~2000 requests 😭

    }

    private void createHeliosJobs() {
        // one job to fetch all cinemas - once a week or less often

        // foreach cinema fetch movies and showings in one request - once a day ~53 requests

    }

    private void createCinemaCityJobs() {
        // one job to fetch all cinemas - once a week or less often

        //if latest cinema fetching job != pending || job != running
        jobRepository.save(Job.cinemaFetchingJob(CinemaChain.CINEMA_CITY));
        var cinemas = cinemaRepository.findByCinemaChain(CinemaChain.CINEMA_CITY);
        cinemas.forEach(cinema -> {
            //if latest movie fetching job != pending || job != running
            jobRepository.save(Job.movieFetchingJob(cinema));
        });
        // foreach cinema fetch movies and showings in one request - once a day ~35 requests
    }

}
