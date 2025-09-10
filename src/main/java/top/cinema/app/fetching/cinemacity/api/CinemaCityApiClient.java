package top.cinema.app.fetching.cinemacity.api;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import top.cinema.app.fetching.cinemacity.model.CinemaCityCinemasRootDto;
import top.cinema.app.fetching.cinemacity.model.CinemaCityMoviesRootDto;

import java.time.LocalDate;

public interface CinemaCityApiClient {

    @GetExchange("/cinemas/with-event/until/{yearAway}")
    ResponseEntity<CinemaCityCinemasRootDto> fetchCinemas(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate yearAway);

    @GetExchange("/film-events/in-cinema/{cinemaId}/at-date/{atDate}")
    ResponseEntity<CinemaCityMoviesRootDto> fetchMovies(@PathVariable Integer cinemaId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate atDate);
}
