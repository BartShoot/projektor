package top.cinema.app.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.dto.MovieFront;
import top.cinema.app.dto.ShowingFront;
import top.cinema.app.entities.core.Movie;
import top.cinema.app.entities.core.Showing;
import top.cinema.app.fetching.movie_data.api.FilmwebApiClient;
import top.cinema.app.fetching.movie_data.api.ImdbApiClient;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movie")
@CrossOrigin(origins = "*")
public class MovieController {

    private final MovieRepository movieRepository;

    private final ShowingRepository showingRepository;

    private final FilmwebApiClient filmwebApiClient;

    private final ImdbApiClient imdbApiClient;

    public MovieController(
            MovieRepository movieRepository,
            ShowingRepository showingRepository,
            FilmwebApiClient filmwebApiClient,
            ImdbApiClient imdbApiClient) {
        this.movieRepository = movieRepository;
        this.showingRepository = showingRepository;
        this.filmwebApiClient = filmwebApiClient;
        this.imdbApiClient = imdbApiClient;
    }

    @GetMapping
    public ResponseEntity<List<MovieFront>> getAllMovies() {
        return ResponseEntity.ok(
                movieRepository.findAll().stream().map(Movie::toFront).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieFront> getMovieById(@PathVariable Integer id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        return movieOptional.map(movie -> ResponseEntity.ok(movie.toFront())).orElseGet(() -> ResponseEntity.notFound()
                .build());
    }

    @GetMapping("/{id}/showing")
    public ResponseEntity<List<ShowingFront>> getMovieShowings(@PathVariable Integer id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (movieOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return movieOptional
                .map(movie -> ResponseEntity.ok(
                        movie.getShowings().stream().map(Showing::toShortFront).toList()))
                .orElseGet((() -> ResponseEntity.notFound().build()));
    }
}
