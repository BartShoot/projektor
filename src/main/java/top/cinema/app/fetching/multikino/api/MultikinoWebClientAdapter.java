package top.cinema.app.fetching.multikino.api;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import top.cinema.app.fetching.multikino.model.MultikinoCinemasRootDto;
import top.cinema.app.fetching.multikino.model.MultikinoMoviesRootDto;
import top.cinema.app.fetching.multikino.model.MultikinoShowingsRootDto;

@Service
@Profile("live")
public class MultikinoWebClientAdapter implements MultikinoApiPort {

    private final MultikinoApiClient client;

    public MultikinoWebClientAdapter(MultikinoApiClient client) {
        this.client = client;
    }

    @Override
    public MultikinoCinemasRootDto fetchCinemasData() {
        throw new IllegalStateException("Multikino does not work without session");
//        return client.fetchCinemas().getBody();
    }

    @Override
    public MultikinoMoviesRootDto fetchMoviesData(String cinemaId) {
        throw new IllegalStateException("Multikino does not work without session");
//        return client.fetchMoviesData(cinemaId).getBody();
    }

    @Override
    public MultikinoShowingsRootDto fetchShowingsData(String filmId, String cinemaId) {
        throw new IllegalStateException("Multikino does not work without session");
//        return client.fetchShowingsData(filmId, cinemaId).getBody();
    }
}
