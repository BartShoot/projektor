package top.cinema.app.entities.core;

import jakarta.persistence.*;
import org.hibernate.annotations.Formula;
import top.cinema.app.dto.MovieFront;

import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String title;

    @Column(unique = true)
    private String normalizedTitle;

    private String originalTitle;
    private Integer durationMinutes;

    @Column(unique = true)
    private String cinemaCityId;

    @Column(unique = true)
    private Integer heliosId;

    @Column(unique = true)
    private String multikinoId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "externalId", column = @Column(name = "filmweb_id")),
        @AttributeOverride(name = "url", column = @Column(name = "filmweb_url")),
        @AttributeOverride(name = "rating", column = @Column(name = "filmweb_rating")),
        @AttributeOverride(name = "ratingCount", column = @Column(name = "filmweb_rating_count")),
        @AttributeOverride(name = "posterUrl", column = @Column(name = "filmweb_poster_url", length = 511))
    })
    private FilmwebData filmwebData;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "externalId", column = @Column(name = "imdb_id")),
        @AttributeOverride(name = "url", column = @Column(name = "imdb_url")),
        @AttributeOverride(name = "rating", column = @Column(name = "imdb_rating")),
        @AttributeOverride(name = "ratingCount", column = @Column(name = "imdb_rating_count")),
        @AttributeOverride(name = "posterUrl", column = @Column(name = "imdb_poster_url", length = 511))
    })
    private IMDbData imdbData;

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Showing> showings;

    // TODO: check if i want cities

    @Formula("(SELECT COUNT(s.id) FROM showings as s WHERE s.movie_id = id)")
    private Integer showingsCount;

    public Movie() {}

    public Movie(String name, String normalizedTitle, Integer durationMinutes) {
        this.title = name;
        this.normalizedTitle = normalizedTitle;
        this.originalTitle = name;
        this.durationMinutes = durationMinutes;
    }

    public Movie(String title, String normalizedTitle, String originalTitle, Integer durationMinutes) {
        this.title = title;
        this.normalizedTitle = normalizedTitle;
        this.originalTitle = originalTitle;
        this.durationMinutes = durationMinutes;
    }

    public MovieFront toFront() {
        return new MovieFront(
                id,
                title,
                durationMinutes,
                null,
                showingsCount,
                filmwebData != null
                        ? new MovieFront.ExternalSourceData(
                                filmwebData.getUrl(),
                                filmwebData.getRating(),
                                filmwebData.getRatingCount(),
                                filmwebData.getPosterUrl())
                        : null,
                imdbData != null
                        ? new MovieFront.ExternalSourceData(
                                imdbData.getUrl(),
                                imdbData.getRating(),
                                imdbData.getRatingCount(),
                                imdbData.getPosterUrl())
                        : null);
    }

    public MovieFront toFrontWithShowings() {
        return new MovieFront(
                id,
                title,
                durationMinutes,
                showings.stream().map(Showing::toFront).toList(),
                showingsCount,
                filmwebData != null
                        ? new MovieFront.ExternalSourceData(
                                filmwebData.getUrl(),
                                filmwebData.getRating(),
                                filmwebData.getRatingCount(),
                                filmwebData.getPosterUrl())
                        : null,
                imdbData != null
                        ? new MovieFront.ExternalSourceData(
                                imdbData.getUrl(),
                                imdbData.getRating(),
                                imdbData.getRatingCount(),
                                imdbData.getPosterUrl())
                        : null);
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public String getCinemaCityId() {
        return cinemaCityId;
    }

    public void setCinemaCityId(String cinemaCityId) {
        this.cinemaCityId = cinemaCityId;
    }

    public Integer getHeliosId() {
        return heliosId;
    }

    public void setHeliosId(Integer heliosId) {
        this.heliosId = heliosId;
    }

    public String getMultikinoId() {
        return multikinoId;
    }

    public void setMultikinoId(String multikinoId) {
        this.multikinoId = multikinoId;
    }

    public List<Showing> getShowings() {
        return showings;
    }

    public String getNormalizedTitle() {
        return normalizedTitle;
    }

    public void setNormalizedTitle(String normalizedTitle) {
        this.normalizedTitle = normalizedTitle;
    }

    public Integer getShowingsCount() {
        return showingsCount;
    }

    public FilmwebData getFilmwebData() {
        return filmwebData;
    }

    public void setFilmwebData(FilmwebData filmwebData) {
        this.filmwebData = filmwebData;
    }

    public IMDbData getImdbData() {
        return imdbData;
    }

    public void setImdbData(IMDbData imdbData) {
        this.imdbData = imdbData;
    }
}
