package top.cinema.app.fetching.movie_data.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import top.cinema.app.fetching.movie_data.model.IMDbInterests;
import top.cinema.app.fetching.movie_data.model.IMDbMovieDetails;
import top.cinema.app.fetching.movie_data.model.IMDbSearchResults;

public interface ImdbApiClient {

    @GetExchange("/search/titles")
    ResponseEntity<IMDbSearchResults> search(@RequestParam String query, @RequestParam Integer limit);

    @GetExchange("/titles/{id}")
    ResponseEntity<IMDbMovieDetails> fetchDetails(@PathVariable String id);

    @GetExchange("/interests")
    ResponseEntity<IMDbInterests> fetchInterests();
}
