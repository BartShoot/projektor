package top.cinema.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.cinema.app.fetching.multikino.api.MultikinoApiAdapter;
import top.cinema.app.fetching.multikino.model.MultikinoCinemaDto;
import top.cinema.app.fetching.multikino.model.MultikinoCinemasRootDto;

import static org.junit.jupiter.api.Assertions.*;

class FileMultikinoApiAdapterTest {

    private MultikinoApiAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new MultikinoApiAdapter();
    }

    @Test
    void fetchCinemasData_shouldDeserializeSuccessfully() {
        MultikinoCinemasRootDto cinemasRootDto = null;
        try {
            cinemasRootDto = adapter.fetchCinemasData();
        } catch (Exception e) {
            fail("Deserialization of cinemas.json failed: " + e.getMessage(), e);
        }
        cinemasRootDto.result().forEach(group -> group.cinemas().forEach(it -> System.out.println(it.cinemaName())));

        assertNotNull(cinemasRootDto, "CinemasRootDto should not be null");
        assertNotNull(cinemasRootDto.result(), "Cinema data list should not be null");
        assertFalse(cinemasRootDto.result().isEmpty(), "Cinema data list should not be empty");

        // Basic check on the first cinema
        MultikinoCinemaDto firstCinema = cinemasRootDto.result().getFirst().cinemas().getFirst();
        assertNotNull(firstCinema.cinemaName(), "First cinema name should not be null");
        System.out.println("Successfully deserialized cinemas.json. First cinema: " + firstCinema.cinemaName());
    }
}
