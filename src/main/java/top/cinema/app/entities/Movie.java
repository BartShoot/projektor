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
    private String originalTitle;
    private Integer durationMinutes;

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Showing> showings;

    // TODO: check if i want cities

    @Formula("(SELECT COUNT(s.id) FROM showings as s WHERE s.movie_id = id)")
    private Integer showingsCount;

    public Movie() {

    }

    public Movie(String name, Integer durationMinutes) {
        this.title = name;
        this.originalTitle = name;
        this.durationMinutes = durationMinutes;
    }

    public Movie(String title, String originalTitle, Integer durationMinutes) {
        this.title = title;
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

    public List<Showing> getShowings() {
        return showings;
    }
}
