package top.cinema.app.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.dto.MovieFront;
import top.cinema.app.dto.ShowingFront;
import top.cinema.app.entities.Movie;
import top.cinema.app.entities.Showing;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movie")
@CrossOrigin(origins = "http://localhost:3000")
public class MovieController {

    private final MovieRepository movieRepository;

    private final ShowingRepository showingRepository;

    public MovieController(MovieRepository movieRepository, ShowingRepository showingRepository) {
        this.movieRepository = movieRepository;
        this.showingRepository = showingRepository;
    }

    @GetMapping
    public ResponseEntity<List<MovieFront>> getAllMovies() {
        return ResponseEntity.ok(movieRepository.findAll().stream().map(Movie::toFront).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieFront> getMovieById(@PathVariable Integer id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        return movieOptional.map(movie -> ResponseEntity.ok(movie.toFront())).orElseGet(
                () -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/showing")
    public ResponseEntity<List<ShowingFront>> getMovieShowings(@PathVariable Integer id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        var test = showingRepository.findAll();
        if (movieOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return movieOptional.map(
                movie -> ResponseEntity.ok(movie.getShowings().stream().map(Showing::toFront).toList())).orElseGet(
                (() -> ResponseEntity.notFound().build()));
    }
}
