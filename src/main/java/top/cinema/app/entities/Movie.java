package top.cinema.app.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String title;
    private Integer durationMinutes;

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Showing> showings;

    // TODO: check if i want cities

    @Formula("(SELECT COUNT(s.id) FROM showings as s WHERE s.movie_id = id)")
    private Integer showingsCount;

    public Movie() {

    }

    public Movie(String name, Integer durationMinutes){
        this.title = name;
        this.durationMinutes = durationMinutes;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public List<Showing> getShowings() {
        return showings;
    }
}
