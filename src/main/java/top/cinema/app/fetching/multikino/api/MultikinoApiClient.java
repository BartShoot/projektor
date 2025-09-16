package top.cinema.app.fetching.multikino.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import top.cinema.app.fetching.multikino.model.MultikinoCinemasRootDto;
import top.cinema.app.fetching.multikino.model.MultikinoMoviesRootDto;
import top.cinema.app.fetching.multikino.model.MultikinoShowingsRootDto;

public interface MultikinoApiClient {

    @GetExchange("/microservice/showings/cinemas")
    ResponseEntity<MultikinoCinemasRootDto> fetchCinemas();

    @GetExchange("/microservice/showings/films")
    ResponseEntity<MultikinoMoviesRootDto> fetchMoviesData(@RequestParam("cinemaId") String cinemaId);

    @GetExchange("/microservice/showings/cinemas/{cinemaId}/films")
    ResponseEntity<MultikinoShowingsRootDto> fetchShowingsData(@RequestParam("filmId") String filmId, @PathVariable("cinemaId") String cinemaId);
}
