package top.cinema.app.admin;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.cinema.app.dao.CinemaRepository;
import top.cinema.app.dao.CityRepository;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.dto.MovieFront;
import top.cinema.app.entities.core.*;
import top.cinema.app.fetching.dao.JobRepository;
import top.cinema.app.fetching.durable_jobs.Job;
import top.cinema.app.fetching.movie_data.api.FilmwebApiClient;
import top.cinema.app.fetching.movie_data.api.ImdbApiClient;
import top.cinema.app.fetching.movie_data.model.FilmwebMoviePreview;
import top.cinema.app.fetching.movie_data.model.FilmwebSearchResults;
import top.cinema.app.fetching.movie_data.service.MovieDataUpdater;
import top.cinema.app.model.CinemaChain;

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
    private final JobRepository jobRepository;
    private final MovieDataUpdater movieUpdater;

    public record FilmwebSearchResultView(Integer id, FilmwebMoviePreview preview) {}

    public AdminController(
            CityRepository cityRepository,
            CinemaRepository cinemaRepository,
            MovieRepository movieRepository,
            ShowingRepository showingRepository,
            FilmwebApiClient filmwebApiClient,
            ImdbApiClient imdbApiClient,
            JobRepository jobRepository,
            MovieDataUpdater movieUpdater) {
        this.cityRepository = cityRepository;
        this.cinemaRepository = cinemaRepository;
        this.movieRepository = movieRepository;
        this.showingRepository = showingRepository;
        this.filmwebApiClient = filmwebApiClient;
        this.imdbApiClient = imdbApiClient;
        this.jobRepository = jobRepository;
        this.movieUpdater = movieUpdater;
    }

    private String getInitialFragmentUrl(HttpServletRequest request) {
        String path = request.getRequestURI();
        String query = request.getQueryString();
        return path + (query != null ? "?" + query : "");
    }

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("initialFragmentUrl", "");
        return "admin/dashboard";
    }

    @GetMapping("/cities")
    public String getCities(
            Model model,
            @RequestHeader(name = "HX-Request", required = false) String hxRequest,
            HttpServletRequest request) {
        model.addAttribute(
                "cities", cityRepository.findAll().stream().map(City::toFront).toList());
        if (hxRequest != null) {
            return "admin/fragments :: cities-table";
        } else {
            model.addAttribute("initialFragmentUrl", getInitialFragmentUrl(request));
            return "admin/dashboard";
        }
    }

    @GetMapping("/cinemas")
    public String getCinemas(
            Model model,
            @RequestHeader(name = "HX-Request", required = false) String hxRequest,
            HttpServletRequest request) {
        model.addAttribute(
                "cinemas",
                cinemaRepository.findAll().stream().map(Cinema::toFront).toList());
        if (hxRequest != null) {
            return "admin/fragments :: cinemas-table";
        } else {
            model.addAttribute("initialFragmentUrl", getInitialFragmentUrl(request));
            return "admin/dashboard";
        }
    }

    @GetMapping("/movies")
    public String getMovies(
            Model model,
            @RequestParam(name = "showAll", defaultValue = "false") boolean showAll,
            @RequestHeader(name = "HX-Request", required = false) String hxRequest,
            HttpServletRequest request) {
        List<Movie> movies;
        if (showAll) {
            movies = movieRepository.findAll();
        } else {
            movies = movieRepository.findMoviesWithFutureShowings();
        }
        model.addAttribute(
                "movies", movies.stream().map(Movie::toFront).toList());
        model.addAttribute("showAll", showAll);
        if (hxRequest != null) {
            return "admin/fragments :: movies-table";
        } else {
            model.addAttribute("initialFragmentUrl", getInitialFragmentUrl(request));
            return "admin/dashboard";
        }
    }

    @GetMapping("/movie/{id}/edit")
    public String showEditMoviePage(
            @PathVariable Integer id,
            Model model,
            @RequestHeader(name = "HX-Request", required = false) String hxRequest,
            HttpServletRequest request) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (movieOptional.isEmpty()) {
            return "redirect:/admin/movies";
        }
        Movie movie = movieOptional.get();

        var filmwebData = movie.getFilmwebData();
        var imdbData = movie.getImdbData();

        MovieFront.ExternalSourceData filmweb = null;
        if (filmwebData != null) {
            filmweb = new MovieFront.ExternalSourceData(filmwebData.getExternalId(), filmwebData.getUrl(), filmwebData.getRating(), filmwebData.getRatingCount(), filmwebData.getPosterUrl());
        }

        MovieFront.ExternalSourceData imdb = null;
        if (imdbData != null) {
            imdb = new MovieFront.ExternalSourceData(imdbData.getExternalId(), imdbData.getUrl(), imdbData.getRating(), imdbData.getRatingCount(), imdbData.getPosterUrl());
        }

        MovieFront movieFront = new MovieFront(
                movie.getId(),
                movie.getTitle(),
                movie.getDurationMinutes(),
                null, // showings list - not needed for edit page
                movie.getShowingsCount(),
                filmweb,
                imdb
        );

        model.addAttribute("movie", movieFront);

        if (hxRequest != null) {
            return "admin/edit-movie :: edit-movie-container";
        } else {
            model.addAttribute("initialFragmentUrl", getInitialFragmentUrl(request));
            return "admin/dashboard";
        }
    }

    @PostMapping("/movie/{id}/update")
    public String updateMovie(
            @PathVariable Integer id,
            @RequestParam(required = false) String imdbId,
            @RequestParam(required = false) String filmwebId,
            Model model) {

        movieUpdater.updateMovie(id, imdbId, filmwebId);

        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (movieOptional.isEmpty()) {
            return "redirect:/admin/movies";
        }
        Movie movie = movieOptional.get();

        var filmwebData = movie.getFilmwebData();
        var imdbData = movie.getImdbData();

        MovieFront.ExternalSourceData filmweb = null;
        if (filmwebData != null) {
            filmweb = new MovieFront.ExternalSourceData(filmwebData.getExternalId(), filmwebData.getUrl(), filmwebData.getRating(), filmwebData.getRatingCount(), filmwebData.getPosterUrl());
        }

        MovieFront.ExternalSourceData imdb = null;
        if (imdbData != null) {
            imdb = new MovieFront.ExternalSourceData(imdbData.getExternalId(), imdbData.getUrl(), imdbData.getRating(), imdbData.getRatingCount(), imdbData.getPosterUrl());
        }

        MovieFront movieFront = new MovieFront(
                movie.getId(),
                movie.getTitle(),
                movie.getDurationMinutes(),
                null,
                movie.getShowingsCount(),
                filmweb,
                imdb
        );

        model.addAttribute("movie", movieFront);
        return "admin/edit-movie :: edit-movie-container";
    }

    @GetMapping("/showings")
    public String getShowings(
            Model model,
            @RequestParam(name = "cinemaId", required = false) Integer cinemaId,
            @RequestHeader(name = "HX-Request", required = false) String hxRequest,
            HttpServletRequest request) {
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

        if (hxRequest != null) {
            return "admin/fragments :: showings-view";
        } else {
            model.addAttribute("initialFragmentUrl", getInitialFragmentUrl(request));
            return "admin/dashboard";
        }
    }

    @GetMapping("/jobs")
    public String getJobs(
            Model model,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String cinemaChain,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestHeader(name = "HX-Request", required = false) String hxRequest,
            HttpServletRequest request) {

        Job.Status statusEnum = null;
        if (status != null && !status.isEmpty()) {
            try {
                statusEnum = Job.Status.valueOf(status);
            } catch (IllegalArgumentException e) {
                // Handle invalid status string
            }
        }
        CinemaChain cinemaChainEnum = null;
        if (cinemaChain != null && !cinemaChain.isEmpty()) {
            try {
                cinemaChainEnum = CinemaChain.valueOf(cinemaChain);
            } catch (IllegalArgumentException e) {
                // Handle invalid cinemaChain string
            }
        }
        Page<Job> jobsPage;
        PageRequest pageable = PageRequest.of(page, size);
        if (statusEnum != null && cinemaChainEnum != null) {
            jobsPage = jobRepository.findByStatusAndCinemaChain(statusEnum, cinemaChainEnum, pageable);
        } else if (statusEnum != null) {
            jobsPage = jobRepository.findByStatus(statusEnum, pageable);
        } else if (cinemaChainEnum != null) {
            jobsPage = jobRepository.findByCinemaChain(cinemaChainEnum, pageable);
        } else {
            jobsPage = jobRepository.findAll(pageable);
        }

        model.addAttribute("jobs", jobsPage);
        model.addAttribute("status", status);
        model.addAttribute("cinemaChain", cinemaChain);

        if (hxRequest != null) {
            return "admin/fragments :: jobs-table";
        } else {
            model.addAttribute("initialFragmentUrl", getInitialFragmentUrl(request));
            return "admin/dashboard";
        }
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
                        it.id(), filmwebApiClient.fetchPreview(it.id().toString()).getBody()))
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
