package top.cinema.app.fetching.cinemacity.api;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import top.cinema.app.fetching.cinemacity.model.CinemaCityCinemasRootDto;
import top.cinema.app.fetching.cinemacity.model.CinemaCityMoviesRootDto;

import java.time.LocalDate;

@Service
@Profile("live")
public class CinemaCityWebClientAdapter implements CinemaCityApiPort {

    private final CinemaCityApiClient apiClient;

    public CinemaCityWebClientAdapter(CinemaCityApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public CinemaCityCinemasRootDto fetchCinemasData() {
        LocalDate yearAway = LocalDate.now().plusYears(1);
        return apiClient.fetchCinemas(yearAway).getBody();
    }

    @Override
    public CinemaCityMoviesRootDto fetchMoviesData(Integer cinemaId, LocalDate showingsDate) {
        return apiClient.fetchMovies(cinemaId, showingsDate).getBody();
    }
}
