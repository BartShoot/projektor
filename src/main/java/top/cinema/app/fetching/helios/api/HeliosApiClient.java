package top.cinema.app.fetching.helios.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import top.cinema.app.fetching.helios.model.CinemasRootDto;
import top.cinema.app.fetching.helios.model.ShowingsRootDto;

public interface HeliosApiClient {
    @GetExchange("/cinemas")
    CinemasRootDto fetchCinemas();

    @GetExchange("/cinemas/{cinemaId}/screenings")
    ShowingsRootDto fetchShowings(@PathVariable Integer cinemaId);
}
