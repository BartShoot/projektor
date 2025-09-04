package top.cinema.app;

import org.junit.Test;
import top.cinema.app.entities.durable_jobs.Resource;
import top.cinema.app.model.CinemaChain;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceUrlTest {

    @Test
    public void cinemaCityUrl() {
        var resource = new Resource(Resource.Type.CINEMAS, CinemaChain.CINEMA_CITY);
        String ccBaseUrl = "https://www.cinema-city.pl/pl/data-api-service/v1/quickbook/";
        LocalDate yearAway = LocalDate.now().plusYears(1);
        String date = String.format(
                "%s-%02d-%02d", yearAway.getYear(), yearAway.getMonth().getValue(), yearAway.getDayOfMonth());
        assertEquals(
                ccBaseUrl + "10103/cinemas/with-event/until/" + date + "?attr=&lang=pl_PL",
                resource.getUrl());
    }

    @Test
    public void heliosUrl() {
        var resource = new Resource(Resource.Type.CINEMAS, CinemaChain.HELIOS);
        String heliosBaseUrl = "https://api.helios.pl/api/v1/cinemas";
        assertEquals(heliosBaseUrl, resource.getUrl());
    }

    @Test
    public void multikinoUrl() {
        var resource = new Resource(Resource.Type.CINEMAS, CinemaChain.MULTIKINO);
        String multikinoBaseUrl = "https://multikino.pl/api/microservice/showings/cinemas";
        assertEquals(multikinoBaseUrl, resource.getUrl());
    }
}
