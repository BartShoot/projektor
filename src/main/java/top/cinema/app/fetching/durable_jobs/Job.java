package top.cinema.app.fetching.durable_jobs;

import jakarta.persistence.*;
import top.cinema.app.entities.core.Cinema;
import top.cinema.app.model.CinemaChain;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
public class Job {

    public enum Type {
        CINEMAS, MOVIES, SHOWINGS, MOVIES_SHOWINGS
    }

    public enum Status {
        PENDING, RUNNING, SUCCESS, FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private Type type;

    private Status status;

    private CinemaChain cinemaChain;

    private Integer cityId;

    private String cinemaId;

    private Integer movieId;

    private LocalDateTime createDate;

    private LocalDateTime lastUpdateDate;

    public Job() {

    }

    public static Job cinemaFetchingJob(CinemaChain cinemaChain) {
        var job = new Job();
        job.cinemaChain = cinemaChain;
        LocalDateTime now = LocalDateTime.now();
        job.setCreateDate(now);
        job.setLastUpdateDate(now);
        job.setStatus(Job.Status.PENDING);
        job.setType(Type.CINEMAS);
        return job;
    }

    public static Job movieFetchingJob(Cinema cinema) {
        var job = new Job();
        job.cinemaChain = cinema.getCinemaChain();
        job.cityId = cinema.getCity().getId();
        LocalDateTime now = LocalDateTime.now();
        job.setCreateDate(now);
        job.setLastUpdateDate(now);
        job.setStatus(Job.Status.PENDING);
        job.setType(Type.MOVIES);
        return job;
    }

    public static Job movieShowingsJob(Cinema cinema) {
        var job = new Job();
        job.cinemaChain = cinema.getCinemaChain();
        job.cityId = cinema.getCity().getId();
        job.cinemaId = cinema.getExternalId();
        LocalDateTime now = LocalDateTime.now();
        job.setCreateDate(now);
        job.setLastUpdateDate(now);
        job.setStatus(Job.Status.PENDING);
        job.setType(Type.MOVIES_SHOWINGS);
        return job;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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


    public String getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
