package top.cinema.app.entities.durable_jobs;

import jakarta.persistence.*;
import org.apache.commons.lang3.NotImplementedException;
import top.cinema.app.entities.core.Cinema;
import top.cinema.app.model.CinemaChain;

import java.time.LocalDate;

@Entity
@Table(name = "resources")
public class Resource {

    private static final String CINEMA_CITY_BASE_URL =
            "https://www.cinema-city.pl/pl/data-api-service/v1/quickbook/10103/";

    private static final String MULTIKINO_BASE_URL = "https://multikino.pl/api/microservice/";

    private static final String HELIOS_BASE_URL = "https://api.helios.pl/api/v1/";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    private Type type;

    private String url;

    public Resource(Type type, CinemaChain cinemaChain) {
        this.type = type;
        this.url = switch (cinemaChain) {
            case INDEPENDENT -> throw new NotImplementedException("No independent cinema support");
            case CINEMA_CITY -> getCinemasCinemaCityUrl();
            case MULTIKINO -> getCinemasMultikinoUrl();
            case HELIOS -> getCinemasHeliosUrl();
        };
    }

    public Resource(Type type, Cinema cinema) {
        this.type = type;
        this.url = switch (cinema.getCinemaChain()) {
            case INDEPENDENT -> throw new NotImplementedException("No independent cinema support");
            case CINEMA_CITY -> getMoviesCinemaCityUrl(cinema);
            case MULTIKINO -> getMoviesMultikinoUrl(cinema);
            case HELIOS -> getMoviesHelios(cinema);
        };
    }

    private String getMoviesHelios(Cinema cinema) {
        return getCinemasHeliosUrl() + "/%d/screenings".formatted(cinema.getId());
    }

    private String getMoviesMultikinoUrl(Cinema cinema) {
        return getCinemasMultikinoUrl() + "/films?cinemaId=%s";
    }

    private String getMoviesCinemaCityUrl(Cinema cinema) {
        return CINEMA_CITY_BASE_URL +
                "film-events/in-cinema/%s/at-date/%s?attr=&lang=pl_PL".formatted(cinema.getExternalId(),
                                                                                 LocalDate.now());
    }

    private String getCinemasHeliosUrl() {
        return HELIOS_BASE_URL + "cinemas";
    }

    private String getCinemasMultikinoUrl() {
        return MULTIKINO_BASE_URL + "showings/cinemas";
    }

    private String getCinemasCinemaCityUrl() {
        return CINEMA_CITY_BASE_URL + "cinemas/with-event/until/" +
                LocalDate.now().plusYears(1) + "?attr=&lang=pl_PL";
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public enum Type {
        CINEMAS, MOVIES, SHOWINGS
    }

}
