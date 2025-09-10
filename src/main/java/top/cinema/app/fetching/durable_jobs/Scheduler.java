package top.cinema.app.fetching.durable_jobs;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.cinema.app.dao.CinemaRepository;
import top.cinema.app.fetching.cinemacity.CinemaCityJobProcessor;
import top.cinema.app.fetching.dao.JobRepository;
import top.cinema.app.model.CinemaChain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Collection;
import java.util.Optional;

@Service
public class Scheduler {
    private final JobRepository jobRepository;
    private final CinemaRepository cinemaRepository;
    private final CinemaCityJobProcessor ccJobProcessor;

    public Scheduler(JobRepository jobRepository, CinemaRepository cinemaRepository,
                     CinemaCityJobProcessor ccJobProcessor) {
        this.jobRepository = jobRepository;
        this.cinemaRepository = cinemaRepository;
        this.ccJobProcessor = ccJobProcessor;
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void processJobs() {
        System.out.println("Processing jobs...");
        var pageable = PageRequest.of(0, 5, Sort.by("createDate").ascending());
        Collection<Job> ccJobs = jobRepository.findByStatusAndCinemaChain(Job.Status.PENDING,
                CinemaChain.CINEMA_CITY,
                pageable);
        ccJobs.forEach(job -> job.setStatus(Job.Status.RUNNING));
        jobRepository.saveAll(ccJobs);
        ccJobProcessor.process(ccJobs);
        // maybe foreach cinema chain separately
        // var jobs = jobRepository.getOldestPending
        // switch (job.cinemaChain) {
        // switch (job.resource.getFirst().type
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void createJobs() {
        // TODO make all running jobs mark as something else
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
        Optional<Job> lastJob = findLastSuccessfulJob(CinemaChain.CINEMA_CITY, Job.Type.CINEMAS);
        if (lastJob.isEmpty() || lastJob.get().getLastUpdateDate().isBefore(LocalDateTime.now().minusDays(7))) {
            jobRepository.save(Job.cinemaFetchingJob(CinemaChain.CINEMA_CITY));
        }
        LocalDateTime lastWednesday = LocalDate.now().with(
                TemporalAdjusters.previousOrSame(DayOfWeek.WEDNESDAY)).atStartOfDay();
        Optional<Job> lastShowingsJob = findLastSuccessfulJob(CinemaChain.CINEMA_CITY, Job.Type.MOVIES_SHOWINGS);
        if (lastShowingsJob.isEmpty() || lastShowingsJob.get().getLastUpdateDate().isBefore(lastWednesday)) {
            var cinemas = cinemaRepository.findByCinemaChain(CinemaChain.CINEMA_CITY);
            cinemas.forEach(cinema -> {
                //if latest movie fetching job != pending || job != running
                jobRepository.save(Job.movieShowingsJob(cinema));
            });
        }
    }

    private Optional<Job> findLastSuccessfulJob(CinemaChain cinemaChain, Job.Type type) {
        return jobRepository.findLastJob(cinemaChain, type, Job.Status.SUCCESS);
    }

}
