package top.cinema.app.fetching.durable_jobs;

import jakarta.persistence.*;
import top.cinema.app.entities.Cinema;
import top.cinema.app.model.CinemaChain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {


    public enum Status {
        PENDING, RUNNING, SUCCESS, FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Collection<Resource> resources;

    private Status status;

    private CinemaChain cinemaChain;

    private Integer cityId;

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
        job.setResource(new Resource(Resource.Type.CINEMAS, cinemaChain));
        job.setStatus(Job.Status.PENDING);
        return job;
    }

    public static Job movieFetchingJob(Cinema cinema) {
        var job = new Job();
        job.cinemaChain = cinema.getCinemaChain();
        job.cityId = cinema.getCity().getId();
        LocalDateTime now = LocalDateTime.now();
        job.setCreateDate(now);
        job.setLastUpdateDate(now);
        job.setResource(new Resource(Resource.Type.MOVIES, cinema));
        job.setStatus(Job.Status.PENDING);
        return job;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Collection<Resource> getResources() {
        return resources;
    }

    public void setResource(Resource resource) {
        this.resources = List.of(resource);
    }

    public void setResources(Collection<Resource> resources) {
        this.resources = resources;
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
