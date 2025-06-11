package top.cinema.app.entities;

import jakarta.persistence.*;
import top.cinema.app.dto.ShowingFront;

import java.time.LocalDateTime;

@Entity
@Table(name = "showings")
public class Showing {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    private LocalDateTime showingTime;

    public Showing() {
    }

    public Showing(Cinema cinema, Movie movie, LocalDateTime showingTime) {
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
}
