package top.cinema.app.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private final String name;

    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
    private List<Cinema> cinemas;

    @Formula("(SELECT COUNT(c.id) FROM cinemas as c WHERE c.city_id = id)")
    private Integer cinemaCount;

    protected City() {
        this.name = "";
    }

    public City(String name) {
        this.name = name;
        this.cinemas = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getCinemaCount() {
        return cinemaCount;
    }

    public void setCinemaCount(Integer cinemaCount) {
        this.cinemaCount = cinemaCount;
    }

    public List<Cinema> getCinemas() {
        return cinemas;
    }

    public void setCinemas(List<Cinema> cinemas) {
        this.cinemas = cinemas;
    }

    public void addCinema(Cinema cinema) {
        if (this.cinemas == null) {
            this.cinemas = new ArrayList<>();
        }
        this.cinemas.add(cinema);
    }
}
