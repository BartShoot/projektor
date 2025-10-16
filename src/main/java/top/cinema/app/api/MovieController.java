package top.cinema.app.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.cinema.app.api.assembler.MovieSummaryDtoAssembler;
import top.cinema.app.api.assembler.ShowingSummaryDtoAssembler;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.dto.model.MovieDto;
import top.cinema.app.dto.model.MovieSummaryDto;
import top.cinema.app.dto.model.ShowingSummaryDto;
import top.cinema.app.entities.core.Movie;
import top.cinema.app.entities.core.Showing;

import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/movie")
@CrossOrigin(origins = "*")
public class MovieController {

    private final MovieRepository movieRepository;
    private final ShowingRepository showingRepository;
    private final MovieSummaryDtoAssembler movieSummaryDtoAssembler;
    private final ShowingSummaryDtoAssembler showingSummaryDtoAssembler;
    private final PagedResourcesAssembler<Movie> moviePagedResourcesAssembler;
    private final PagedResourcesAssembler<Showing> showingPagedResourcesAssembler;

    public MovieController(
            MovieRepository movieRepository,
            ShowingRepository showingRepository,
            MovieSummaryDtoAssembler movieSummaryDtoAssembler,
            ShowingSummaryDtoAssembler showingSummaryDtoAssembler,
            PagedResourcesAssembler<Movie> moviePagedResourcesAssembler,
            PagedResourcesAssembler<Showing> showingPagedResourcesAssembler) {
        this.movieRepository = movieRepository;
        this.showingRepository = showingRepository;
        this.movieSummaryDtoAssembler = movieSummaryDtoAssembler;
        this.showingSummaryDtoAssembler = showingSummaryDtoAssembler;
        this.moviePagedResourcesAssembler = moviePagedResourcesAssembler;
        this.showingPagedResourcesAssembler = showingPagedResourcesAssembler;
    }

    @GetMapping
    public PagedModel<MovieSummaryDto> getAllMovies(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findMoviesWithFutureShowings(pageable);
        return moviePagedResourcesAssembler.toModel(moviePage, movieSummaryDtoAssembler);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Integer id) {
        return movieRepository
                .findById(id)
                .map(Movie::toDto)
                .map(dto -> {
                    dto.add(linkTo(methodOn(MovieController.class).getMovieById(id))
                            .withSelfRel());
                    dto.add(linkTo(methodOn(MovieController.class).getMovieShowings(id, Pageable.unpaged()))
                            .withRel("showings"));
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/showings")
    public PagedModel<ShowingSummaryDto> getMovieShowings(@PathVariable Integer id, Pageable pageable) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
        Page<Showing> showingsPage =
                showingRepository.findByMovieAndShowingTimeAfter(movie, LocalDateTime.now(), pageable);
        return showingPagedResourcesAssembler.toModel(showingsPage, showingSummaryDtoAssembler::toModelForMovie);
    }
}
