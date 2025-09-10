package top.cinema.app.entities.core;

import jakarta.persistence.*;
import top.cinema.app.dto.ShowingFront;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "showings",
        indexes = {
                @Index(name = "idx_showing_cinema_time", columnList = "cinema_id, showingTime"),
                @Index(name = "idx_showing_movie_time", columnList = "movie_id, showingTime")
        })
public class Showing {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String externalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    private LocalDateTime showingTime;

    public Showing() {
    }

    public Showing(String externalId, Cinema cinema, Movie movie, LocalDateTime showingTime) {
        this.externalId = externalId;
        this.cinema = cinema;
        this.movie = movie;
        this.showingTime = showingTime;
    }

    public ShowingFront toFront() {
        return new ShowingFront(id, movie.toFront(), null, showingTime);
    }

    public ShowingFront toFrontWithCinema() {
        return new ShowingFront(id, movie.toFront(), cinema.toFrontWithCity(), showingTime);
    }


    public Integer getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public LocalDateTime getShowingTime() {
        return showingTime;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
