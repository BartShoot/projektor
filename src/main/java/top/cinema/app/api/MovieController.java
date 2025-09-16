package top.cinema.app.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.dto.MovieFront;
import top.cinema.app.dto.MovieUpdateCommand;
import top.cinema.app.dto.ShowingFront;
import top.cinema.app.entities.core.Movie;
import top.cinema.app.entities.core.Showing;
import top.cinema.app.fetching.movie_data.api.FilmwebApiClient;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movie")
@CrossOrigin(origins = "http://localhost:3000")
public class MovieController {

    private final MovieRepository movieRepository;

    private final ShowingRepository showingRepository;

    private final FilmwebApiClient filmwebApiClient;

    public MovieController(MovieRepository movieRepository, ShowingRepository showingRepository, FilmwebApiClient filmwebApiClient) {
        this.movieRepository = movieRepository;
        this.showingRepository = showingRepository;
        this.filmwebApiClient = filmwebApiClient;
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

    @PostMapping("/{id}")
    public ResponseEntity<?> updateMovieData(@PathVariable Integer id, @RequestBody(required = false) MovieUpdateCommand command) {
        if (command == null) {
            Optional<Movie> byId = movieRepository.findById(id);
            if (byId.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            var movie = byId.get();
            var search = filmwebApiClient.search(movie.getTitle(), 3).getBody();
            var fwId = search.searchHits().stream().findFirst().get().id();
            var preview = filmwebApiClient.fetchPreview(fwId).getBody();
            var rating = filmwebApiClient.fetchRating(fwId).getBody();
            return ResponseEntity.ok(new MovieFront(movie.getId(), movie.getTitle(), movie.getDurationMinutes(), null,
                    preview.getHtmlUrl(fwId), rating.rate(), rating.count(), preview.getPosterUrl()));
        }
        return ResponseEntity.ok(List.of());
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
