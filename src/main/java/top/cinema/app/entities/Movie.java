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

    @Formula("(SELECT COUNT(*) FROM showings WHERE movie_id = id)")
    private Integer showingsCount;

    public Movie() {

    }

}
