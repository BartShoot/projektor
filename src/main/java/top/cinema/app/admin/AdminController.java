package top.cinema.app.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.cinema.app.dao.CinemaRepository;
import top.cinema.app.dao.CityRepository;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.entities.core.Cinema;
import top.cinema.app.entities.core.City;
import top.cinema.app.entities.core.Movie;
import top.cinema.app.entities.core.Showing;

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

    public AdminController(CityRepository cityRepository,
                           CinemaRepository cinemaRepository,
                           MovieRepository movieRepository,
                           ShowingRepository showingRepository) {
        this.cityRepository = cityRepository;
        this.cinemaRepository = cinemaRepository;
        this.movieRepository = movieRepository;
        this.showingRepository = showingRepository;
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
        model.addAttribute("cities", cityRepository.findAll().stream().map(City::toFront).toList());
        return "admin/fragments :: cities-table";
    }

    @GetMapping("/cinemas")
    public String getCinemas(Model model) {
        model.addAttribute("cinemas", cinemaRepository.findAll().stream().map(Cinema::toFront).toList());
        return "admin/fragments :: cinemas-table";
    }

    @GetMapping("/movies")
    public String getMovies(Model model) {
        model.addAttribute("movies", movieRepository.findAll().stream().map(Movie::toFront).toList());
        return "admin/fragments :: movies-table";
    }

    @GetMapping("/showings")
    public String getShowings(Model model, @RequestParam(name = "cinemaId", required = false) Integer cinemaId) {
        model.addAttribute("cinemas", cinemaRepository.findAll().stream().map(Cinema::toFront).toList());
        if (cinemaId != null) {
            Optional<Cinema> cinemaOptional = cinemaRepository.findById(cinemaId);
            if (cinemaOptional.isPresent()) {
                model.addAttribute("showings",
                                   showingRepository.findByCinemaAndShowingTimeAfter(cinemaOptional.get(),
                                                                                     LocalDateTime.now()).stream().map(
                                           Showing::toFront).toList());
            } else {
                model.addAttribute("showings", Collections.emptyList());
            }
        } else {
            model.addAttribute("showings", Collections.emptyList());
        }
        return "admin/fragments :: showings-view";
    }


    /**
     * This endpoint is designed to be called by HTMX.
     * It returns an HTML fragment, not a full page.
     */
    @GetMapping("/api/users")
    public String getUsers(Model model, @RequestHeader(value = "HX-Request", required = false) String hxRequest) {
        // Dummy data for demonstration
        List<String> users = List.of("Alice", "Bob", "Charlie");
        model.addAttribute("users", users);
        model.addAttribute("timestamp", Instant.now().toString());

        // If it's an HTMX request, return the fragment. Otherwise, you could redirect or show an error.
        return "admin/fragments :: user-list";
    }
}
