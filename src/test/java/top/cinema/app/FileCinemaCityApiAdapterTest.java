package top.cinema.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.cinema.app.fetching.cinemacity.api.CinemaCityApiAdapter;
import top.cinema.app.fetching.cinemacity.model.CinemaCityCinemaDto;
import top.cinema.app.fetching.cinemacity.model.CinemaCityCinemasRootDto;

import static org.junit.jupiter.api.Assertions.*;

class FileCinemaCityApiAdapterTest {

    private CinemaCityApiAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new CinemaCityApiAdapter();
    }

    @Test
    void fetchCinemasData_shouldDeserializeSuccessfully() {
        CinemaCityCinemasRootDto cinemasRootDto = null;
        try {
            cinemasRootDto = adapter.fetchCinemasData();
        } catch (Exception e) {
            fail("Deserialization of cinemas.json failed: " + e.getMessage(), e);
        }
        assertNotNull(cinemasRootDto, "CinemasRootDto should not be null");
        assertNotNull(cinemasRootDto.body(), "Cinema body should not be null");
        assertNotNull(cinemasRootDto.body().cinemas(), "Cinema data list should not be null");
        assertFalse(cinemasRootDto.body().cinemas().isEmpty(), "Cinema data list should not be empty");

        // Basic check on the first cinema
        CinemaCityCinemaDto firstCinema = cinemasRootDto.body().cinemas().getFirst();
        assertNotNull(firstCinema.displayName(), "First cinema name should not be null");
        assertNotNull(firstCinema.addressInfo(), "First cinema address should not be null");
        assertNotNull(firstCinema.addressInfo().city(), "First cinema city should not be null");
    }
}
