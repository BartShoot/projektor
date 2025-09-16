package top.cinema.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.cinema.app.fetching.multikino.api.MultikinoStaticAdapter;
import top.cinema.app.fetching.multikino.model.MultikinoCinemaDto;
import top.cinema.app.fetching.multikino.model.MultikinoCinemasRootDto;

import static org.junit.jupiter.api.Assertions.*;

class FileMultikinoStaticAdapterTest {

    private MultikinoStaticAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new MultikinoStaticAdapter();
    }

    @Test
    void fetchCinemasData_shouldDeserializeSuccessfully() {
        MultikinoCinemasRootDto cinemasRootDto = null;
        try {
            cinemasRootDto = adapter.fetchCinemasData();
        } catch (Exception e) {
            fail("Deserialization of cinemas.json failed: " + e.getMessage(), e);
        }
        assertNotNull(cinemasRootDto, "CinemasRootDto should not be null");
        assertNotNull(cinemasRootDto.result(), "Cinema data list should not be null");
        assertFalse(cinemasRootDto.result().isEmpty(), "Cinema data list should not be empty");

        // Basic check on the first cinema
        MultikinoCinemaDto firstCinema = cinemasRootDto.result().getFirst().cinemas().getFirst();
        assertNotNull(firstCinema.cinemaName(), "First cinema name should not be null");
    }
}
