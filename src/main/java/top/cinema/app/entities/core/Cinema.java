package top.cinema.app.entities.core;

import jakarta.persistence.*;
import org.hibernate.annotations.Formula;
import top.cinema.app.dto.CinemaFront;
import top.cinema.app.dto.model.CinemaDto;
import top.cinema.app.dto.model.CinemaSummaryDto;
import top.cinema.app.model.CinemaChain;

import java.util.List;

@Entity
@Table(name = "cinemas")
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String name;

    private String location;

    private String externalId;

    private CinemaChain cinemaChain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(mappedBy = "cinema", fetch = FetchType.LAZY)
    private List<Showing> showings;

    @Formula("(SELECT COUNT(s.id) FROM showings s WHERE s.cinema_id = id AND s.showing_time > NOW())")
    private Integer showingCounts;

    public Cinema() {}

    public Cinema(String name, String address, String externalId, CinemaChain cinemaChain, City city) {
        this.name = name;
        this.location = address;
        this.externalId = externalId;
        this.cinemaChain = cinemaChain;
        this.city = city;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public CinemaFront toFront() {
        return new CinemaFront(id, name, location, externalId, cinemaChain, null, showingCounts, null);
    }

    public CinemaFront toFrontWithCity() {
        return new CinemaFront(id, name, location, externalId, cinemaChain, city.toFront(), showingCounts, null);
    }

    public CinemaSummaryDto toSummaryDto() {
        return new CinemaSummaryDto(id, name, location, showingCounts);
    }

    public CinemaDto toDto() {
        return new CinemaDto(id, name, location, cinemaChain, city.toSummaryDto(), showingCounts);
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public CinemaChain getCinemaChain() {
        return cinemaChain;
    }

    public City getCity() {
        return city;
    }

    public List<Showing> getShowings() {
        return showings;
    }

    public Integer getShowingCounts() {
        return showingCounts;
    }

    public Integer getId() {
        return id;
    }
}
