package top.cinema.app.api;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.cinema.app.api.assembler.CinemaSummaryDtoAssembler;
import top.cinema.app.api.assembler.MovieSummaryDtoAssembler;
import top.cinema.app.api.assembler.ShowingSummaryDtoAssembler;
import top.cinema.app.dao.CinemaRepository;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.dto.model.CinemaDto;
import top.cinema.app.dto.model.CinemaSummaryDto;
import top.cinema.app.dto.model.MovieSummaryDto;
import top.cinema.app.dto.model.ShowingSummaryDto;
import top.cinema.app.entities.core.Cinema;
import top.cinema.app.entities.core.Movie;
import top.cinema.app.entities.core.Showing;

import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/cinema")
@CrossOrigin(origins = "http://localhost:3000")
public class CinemaController {

    private final CinemaRepository cinemaRepository;
    private final ShowingRepository showingRepository;
    private final MovieRepository movieRepository;
    private final CinemaSummaryDtoAssembler cinemaSummaryDtoAssembler;
    private final ShowingSummaryDtoAssembler showingSummaryDtoAssembler;
    private final MovieSummaryDtoAssembler movieSummaryDtoAssembler;
    private final PagedResourcesAssembler<Cinema> cinemaPagedResourcesAssembler;
    private final PagedResourcesAssembler<Showing> showingPagedResourcesAssembler;
    private final PagedResourcesAssembler<Movie> moviePagedResourcesAssembler;

    public CinemaController(
            CinemaRepository cinemaRepository,
            ShowingRepository showingRepository,
            MovieRepository movieRepository,
            CinemaSummaryDtoAssembler cinemaSummaryDtoAssembler,
            ShowingSummaryDtoAssembler showingSummaryDtoAssembler,
            MovieSummaryDtoAssembler movieSummaryDtoAssembler,
            PagedResourcesAssembler<Cinema> cinemaPagedResourcesAssembler,
            PagedResourcesAssembler<Showing> showingPagedResourcesAssembler,
            PagedResourcesAssembler<Movie> moviePagedResourcesAssembler){
        this.cinemaRepository = cinemaRepository;
        this.showingRepository = showingRepository;
        this.movieRepository = movieRepository;
        this.cinemaSummaryDtoAssembler = cinemaSummaryDtoAssembler;
        this.showingSummaryDtoAssembler = showingSummaryDtoAssembler;
        this.movieSummaryDtoAssembler = movieSummaryDtoAssembler;
        this.cinemaPagedResourcesAssembler = cinemaPagedResourcesAssembler;
        this.showingPagedResourcesAssembler = showingPagedResourcesAssembler;
        this.moviePagedResourcesAssembler = moviePagedResourcesAssembler;
    }

    @GetMapping
    public PagedModel<CinemaSummaryDto> getCinemas(@ParameterObject Pageable pageable) {
        Page<Cinema> cinemaPage = cinemaRepository.findAll(pageable);
        return cinemaPagedResourcesAssembler.toModel(cinemaPage, cinemaSummaryDtoAssembler);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CinemaDto> getCinemaById(@PathVariable Integer id) {
        return cinemaRepository
                .findById(id)
                .map(Cinema::toDto)
                .map(dto -> {
                    dto.add(linkTo(methodOn(CinemaController.class).getCinemaById(id))
                            .withSelfRel());
                    dto.add(linkTo(methodOn(CinemaController.class).getCinemaShowings(id, Pageable.unpaged()))
                            .withRel("showings"));
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/showings")
    public PagedModel<ShowingSummaryDto> getCinemaShowings(
            @PathVariable Integer id, @ParameterObject Pageable pageable) {
        Cinema cinema = cinemaRepository.findById(id).orElseThrow(() -> new RuntimeException("Cinema not found"));
        Page<Showing> showingsPage =
                showingRepository.findByCinemaAndShowingTimeAfter(cinema, LocalDateTime.now(), pageable);
        return showingPagedResourcesAssembler.toModel(showingsPage, showingSummaryDtoAssembler);
    }

    @GetMapping("/{id}/movies")
    public PagedModel<MovieSummaryDto> getCinemaMovies(@PathVariable Integer id, @ParameterObject Pageable pageable) {
        Page<Movie> moviesPage = movieRepository.findMoviesByCinemaAndShowingsAfter(id, LocalDateTime.now(), pageable);
        return moviePagedResourcesAssembler.toModel(moviesPage, movieSummaryDtoAssembler);
    }
}
