package top.cinema.app.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.cinema.app.dao.CinemaRepository;
import top.cinema.app.dao.CityRepository;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.entities.core.Cinema;
import top.cinema.app.entities.core.City;
import top.cinema.app.entities.core.Movie;
import top.cinema.app.entities.core.Showing;
import top.cinema.app.fetching.movie_data.api.FilmwebApiClient;
import top.cinema.app.fetching.movie_data.api.ImdbApiClient;
import top.cinema.app.fetching.movie_data.model.FilmwebMoviePreview;
import top.cinema.app.fetching.movie_data.model.FilmwebSearchResults;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CityRepository cityRepository;
    private final CinemaRepository cinemaRepository;
    private final MovieRepository movieRepository;
    private final ShowingRepository showingRepository;
    private final FilmwebApiClient filmwebApiClient;
    private final ImdbApiClient imdbApiClient;

    public record FilmwebSearchResultView(Integer id, FilmwebMoviePreview preview) {}

    public AdminController(
            CityRepository cityRepository,
            CinemaRepository cinemaRepository,
            MovieRepository movieRepository,
            ShowingRepository showingRepository,
            FilmwebApiClient filmwebApiClient,
            ImdbApiClient imdbApiClient) {
        this.cityRepository = cityRepository;
        this.cinemaRepository = cinemaRepository;
        this.movieRepository = movieRepository;
        this.showingRepository = showingRepository;
        this.filmwebApiClient = filmwebApiClient;
        this.imdbApiClient = imdbApiClient;
    }

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/cities")
    public String getCities(Model model) {
        model.addAttribute(
                "cities", cityRepository.findAll().stream().map(City::toFront).toList());
        return "admin/fragments :: cities-table";
    }

    @GetMapping("/cinemas")
    public String getCinemas(Model model) {
        model.addAttribute(
                "cinemas",
                cinemaRepository.findAll().stream().map(Cinema::toFront).toList());
        return "admin/fragments :: cinemas-table";
    }

    @GetMapping("/movies")
    public String getMovies(Model model) {
        model.addAttribute(
                "movies", movieRepository.findAll().stream().map(Movie::toFront).toList());
        return "admin/fragments :: movies-table";
    }

    @GetMapping("/movie/{id}/edit")
    public String showEditMoviePage(@PathVariable Integer id, Model model) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (movieOptional.isEmpty()) {
            return "redirect:/admin/movies";
        }
        Movie movie = movieOptional.get();
        model.addAttribute("movie", movie.toFront());

        return "admin/edit-movie :: edit-movie-container";
    }

    @GetMapping("/showings")
    public String getShowings(Model model, @RequestParam(name = "cinemaId", required = false) Integer cinemaId) {
        model.addAttribute(
                "cinemas",
                cinemaRepository.findAll().stream().map(Cinema::toFront).toList());
        if (cinemaId != null) {
            Optional<Cinema> cinemaOptional = cinemaRepository.findById(cinemaId);
            if (cinemaOptional.isPresent()) {
                model.addAttribute(
                        "showings",
                        showingRepository
                                .findByCinemaAndShowingTimeAfter(cinemaOptional.get(), LocalDateTime.now())
                                .stream()
                                .map(Showing::toFront)
                                .toList());
            } else {
                model.addAttribute("showings", Collections.emptyList());
            }
        } else {
            model.addAttribute("showings", Collections.emptyList());
        }
        return "admin/fragments :: showings-view";
    }

    @GetMapping("/search/filmweb")
    public String searchFilmweb(
            @RequestParam Integer movieId, @RequestParam(required = false) String query, Model model) {
        Optional<Movie> movieOptional = movieRepository.findById(movieId);
        if (movieOptional.isEmpty()) {
            return "admin/edit-movie :: filmweb-search-results"; // Return empty fragment
        }
        Movie movie = movieOptional.get();
        model.addAttribute("movie", movie.toFront());

        String searchQuery = (query == null || query.isBlank()) ? movie.getTitle() : query;

        FilmwebSearchResults filmwebSearch =
                filmwebApiClient.search(searchQuery, 5).getBody();
        List<FilmwebSearchResultView> filmwebResults = filmwebSearch.searchHits().stream()
                .filter(it -> it.type().equals("film"))
                .map(it -> new FilmwebSearchResultView(
                        it.id(), filmwebApiClient.fetchPreview(it.id()).getBody()))
                .toList();
        model.addAttribute("filmwebResults", filmwebResults);
        return "admin/edit-movie :: filmweb-search-results";
    }

    @GetMapping("/search/imdb")
    public String searchImdb(@RequestParam Integer movieId, @RequestParam(required = false) String query, Model model) {
        Optional<Movie> movieOptional = movieRepository.findById(movieId);
        if (movieOptional.isEmpty()) {
            return "admin/edit-movie :: imdb-search-results"; // Return empty fragment
        }
        Movie movie = movieOptional.get();
        model.addAttribute("movie", movie.toFront());

        String searchQuery = (query == null || query.isBlank()) ? movie.getTitle() : query;

        model.addAttribute("imdbResults", imdbApiClient.search(searchQuery, 5).getBody());
        return "admin/edit-movie :: imdb-search-results";
    }

}
