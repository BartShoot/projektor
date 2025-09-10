package top.cinema.app.fetching.helios.api;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import top.cinema.app.fetching.helios.model.CinemasRootDto;
import top.cinema.app.fetching.helios.model.ShowingsRootDto;

@Service
@Profile("live")
public class HeliosWebClientAdapter implements HeliosApiPort {

    private final HeliosApiClient apiClient;

    public HeliosWebClientAdapter(HeliosApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public CinemasRootDto fetchCinemasData() {
        return apiClient.fetchCinemas();
    }

    @Override
    public ShowingsRootDto fetchShowingsData(Integer cinemaId) {
        return apiClient.fetchShowings(cinemaId);
    }
}
