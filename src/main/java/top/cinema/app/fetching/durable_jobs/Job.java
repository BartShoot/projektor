package top.cinema.app.fetching.durable_jobs;

import jakarta.persistence.*;
import top.cinema.app.entities.Cinema;
import top.cinema.app.model.CinemaChain;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
public class Job {

    public enum Status {
        PENDING, RUNNING, SUCCESS, FAILED
    }

    public enum Resource {
        CINEMAS, MOVIES, SHOWINGS
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private Resource resource;

    private Status status;

    private CinemaChain cinemaChain;

    private Integer cityId;

    private Integer movieId;

    private LocalDateTime createDate;

    private LocalDateTime lastUpdateDate;

    public Job() {

    }

    public static Job cinemaFetchingJob(Cinema cinema) {
        var job = new Job();
        job.cinemaChain = cinema.getCinemaChain();
        job.setCityId(cinema.getCity().getId());
        LocalDateTime now = LocalDateTime.now();
        job.setCreateDate(now);
        job.setLastUpdateDate(now);
        job.setResource(Job.Resource.CINEMAS);
        job.setStatus(Job.Status.PENDING);
        return job;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public CinemaChain getCinemaChain() {
        return cinemaChain;
    }

    public void setCinemaChain(CinemaChain cinemaChain) {
        this.cinemaChain = cinemaChain;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
