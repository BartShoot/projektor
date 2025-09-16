package top.cinema.app.fetching.movie_data.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import top.cinema.app.fetching.movie_data.model.FilmwebGenre;
import top.cinema.app.fetching.movie_data.model.FilmwebMoviePreview;
import top.cinema.app.fetching.movie_data.model.FilmwebMovieRating;
import top.cinema.app.fetching.movie_data.model.FilmwebSearchResults;

import java.util.Collection;

public interface FilmwebApiClient {

    @GetExchange("/v1/genres")
    ResponseEntity<Collection<FilmwebGenre>> fetchGenres();

    @GetExchange("/v1/live/search")
    ResponseEntity<FilmwebSearchResults> search(@RequestParam String query, @RequestParam Integer pageSize);

    @GetExchange("/v1/film/{id}/preview")
    ResponseEntity<FilmwebMoviePreview> fetchPreview(@PathVariable Integer id);

    @GetExchange("/v1/film/{id}/rating")
    ResponseEntity<FilmwebMovieRating> fetchRating(@PathVariable Integer id);
}
