package top.cinema.app.fetching.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.cinema.app.dao.CinemaRepository;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.entities.core.Cinema;
import top.cinema.app.entities.core.Movie;
import top.cinema.app.entities.core.Showing;
import top.cinema.app.fetching.multikino.api.MultikinoApiPort;

import java.util.Optional;

@Service
public class ShowingSaver {

    private static final Logger log = LoggerFactory.getLogger(ShowingSaver.class);

    private final MultikinoApiPort multikinoApiPort;
    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;
    private final ShowingRepository showingRepository;

    public ShowingSaver(MultikinoApiPort multikinoApiPort,
                        MovieRepository movieRepository,
                        CinemaRepository cinemaRepository,
                        ShowingRepository showingRepository) {
        this.multikinoApiPort = multikinoApiPort;
        this.movieRepository = movieRepository;
        this.cinemaRepository = cinemaRepository;
        this.showingRepository = showingRepository;
    }

    public void processShowings() {
        log.info("Processing showings...");
        saveMultikinoShowings();
    }

    private void saveMultikinoShowings() {
        // one movie
        var showings = multikinoApiPort.fetchShowingsData("HO00002008", "0035").result().getFirst();
        showings.showingGroups().forEach(showing -> {
            showing.sessions().forEach(session -> {
                String string = session.sessionId().toString();
                if (showingRepository.findByExternalId(string).isEmpty()) {
                    Optional<Movie> movie = movieRepository.findByMultikinoId(showings.filmId());
                    Optional<Cinema> cinema = cinemaRepository.findByExternalId("0035"); //is from path
                    showingRepository.save(new Showing(string, cinema.get(), movie.get(), session.startTime()));
                }
            });
        });
    }
}
