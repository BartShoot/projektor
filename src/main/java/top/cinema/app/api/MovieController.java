package top.cinema.app.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.dto.model.MovieSummaryDto;
import top.cinema.app.dto.model.MovieWithShowingsDto;
import top.cinema.app.entities.core.Movie;
import top.cinema.app.entities.core.Showing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movie")
@CrossOrigin(origins = "*")
public class MovieController {

    private final MovieRepository movieRepository;
    private final ShowingRepository showingRepository;

    public MovieController(MovieRepository movieRepository, ShowingRepository showingRepository) {
        this.movieRepository = movieRepository;
        this.showingRepository = showingRepository;
    }

    @GetMapping
    public ResponseEntity<List<MovieSummaryDto>> getAllMovies() {
        return ResponseEntity.ok(movieRepository.findMoviesWithFutureShowings().stream()
                .map(Movie::toSummaryDto)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieWithShowingsDto> getMovieById(@PathVariable Integer id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        return movieOptional
                .map(movie -> {
                    List<Showing> showings =
                            showingRepository.findByMovieAndShowingTimeAfter(movie, LocalDateTime.now());
                    return ResponseEntity.ok(movie.toWithShowingsDto(showings));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
