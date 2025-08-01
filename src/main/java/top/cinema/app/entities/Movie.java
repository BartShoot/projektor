package top.cinema.app.entities;

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

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Showing> showings;

    // TODO: check if i want cities

    @Formula("(SELECT COUNT(s.id) FROM showings as s WHERE s.movie_id = id)")
    private Integer showingsCount;

    public Movie() {

    }

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
        return new MovieFront(id, title, durationMinutes);
    }

    public MovieFront toFrontWithShowings() {
        return new MovieFront(id, title, durationMinutes, showings.stream().map(Showing::toFront).toList());
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
}
