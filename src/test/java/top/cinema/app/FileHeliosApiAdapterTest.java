package top.cinema.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.cinema.app.fetching.helios.api.HeliosApiAdapter;
import top.cinema.app.fetching.helios.model.*;

import static org.junit.jupiter.api.Assertions.*;

class FileHeliosApiAdapterTest {

    private HeliosApiAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new HeliosApiAdapter();
    }

    @Test
    void fetchCinemasData_shouldDeserializeSuccessfully() {
        CinemasRootDto cinemasRootDto = null;
        try {
            cinemasRootDto = adapter.fetchCinemasData();
        } catch (Exception e) {
            fail("Deserialization of cinemas.json failed: " + e.getMessage(), e);
        }
        cinemasRootDto.data().forEach(it -> System.out.println(it.name()));

        assertNotNull(cinemasRootDto, "CinemasRootDto should not be null");
        assertEquals(200, cinemasRootDto.status(), "Status should be 200");
        assertNotNull(cinemasRootDto.data(), "Cinema data list should not be null");
        assertFalse(cinemasRootDto.data().isEmpty(), "Cinema data list should not be empty");

        // Basic check on the first cinema
        CinemaDto firstCinema = cinemasRootDto.data().get(0);
        assertNotNull(firstCinema.name(), "First cinema name should not be null");
        assertNotNull(firstCinema.location(), "First cinema location should not be null");
        assertNotNull(firstCinema.location().city(), "First cinema city should not be null");
        System.out.println("Successfully deserialized cinemas.json. First cinema: " + firstCinema.name());
    }

    @Test
    void fetchShowingsData_shouldDeserializeSuccessfully() {
        ShowingsRootDto showingsRootDto = null;
        try {
            showingsRootDto = adapter.fetchShowingsData();
        } catch (Exception e) {
            fail("Deserialization of showings.json failed: " + e.getMessage(), e);
        }
        showingsRootDto.data().movies().forEach((key, it) -> System.out.println(it.title()));

        assertNotNull(showingsRootDto, "ShowingsRootDto should not be null");
        assertEquals(200, showingsRootDto.status(), "Status should be 200");
        assertNotNull(showingsRootDto.data(), "ShowingsDataPayloadDto should not be null");

        ShowingsDataPayloadDto payload = showingsRootDto.data();
        assertNotNull(payload.screenings(), "Screenings map should not be null");
        assertFalse(payload.screenings().isEmpty(), "Screenings map should not be empty");

        assertNotNull(payload.movies(), "Movies map should not be null");
        assertFalse(payload.movies().isEmpty(), "Movies map should not be empty");

        // Example: Check a specific date if you know it exists in your test file
        String testDate = "2025-06-12"; // Adjust if your test data has a different first date
        if (payload.screenings().containsKey(testDate)) {
            assertNotNull(payload.screenings().get(testDate), "Screenings for " + testDate + " should not be null");
            assertFalse(payload.screenings().get(testDate).isEmpty(),
                    "Screenings for " + testDate + " should not be empty");

            // Further check on a movie/event key if known
            String firstMovieOrEventKey = payload.screenings().get(testDate).keySet().stream().findFirst().orElse(null);
            assertNotNull(firstMovieOrEventKey, "First movie/event key for " + testDate + " should not be null");
            DailyMovieScreeningsDto dailyScreenings = payload.screenings().get(testDate).get(firstMovieOrEventKey);
            assertNotNull(dailyScreenings, "Daily screenings for " + firstMovieOrEventKey + " should not be null");
            assertNotNull(dailyScreenings.screenings(), "Screening entries list should not be null");
        } else {
            System.out.println(
                    "Warning: Test date " + testDate + " not found in showings.json screenings. Skipping detailed check for this date.");
        }


        System.out.println(
                "Successfully deserialized showings.json. Number of dates with screenings: " + payload.screenings().size());
        System.out.println("Number of movies: " + payload.movies().size());
        if (payload.events() != null) {
            System.out.println("Number of events: " + payload.events().size());
        }
        assertNotNull(payload.priceList(), "Price list should not be null");
        assertNotNull(payload.priceList().smartPricing(), "Smart pricing details should not be null");
        assertNotNull(payload.priceList().smartPricing().day1(), "Smart pricing day1 should not be null");

    }
}
