package top.cinema.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.cinema.app.dao.CinemaRepository;
import top.cinema.app.dao.MovieRepository;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.entities.Cinema;
import top.cinema.app.entities.Movie;
import top.cinema.app.entities.Showing;
import top.cinema.app.fetching.cinemacity.api.CinemaCityApiPort;
import top.cinema.app.fetching.helios.api.HeliosApiPort;
import top.cinema.app.fetching.helios.model.ScreeningEntryDto;
import top.cinema.app.fetching.helios.model.ShowingDto;
import top.cinema.app.fetching.helios.model.ShowingsRootDto;
import top.cinema.app.fetching.multikino.api.MultikinoApiPort;

import java.util.List;
import java.util.Optional;

@Service
public class ShowingSaver {

    private static final Logger log = LoggerFactory.getLogger(ShowingSaver.class);

    private final HeliosApiPort heliosApiPort;
    private final CinemaCityApiPort cinemaCityApiPort;
    private final MultikinoApiPort multikinoApiPort;
    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;
    private final ShowingRepository showingRepository;

    public ShowingSaver(HeliosApiPort heliosApiPort,
                        CinemaCityApiPort cinemaCityApiPort,
                        MultikinoApiPort multikinoApiPort,
                        MovieRepository movieRepository,
                        CinemaRepository cinemaRepository,
                        ShowingRepository showingRepository) {
        this.heliosApiPort = heliosApiPort;
        this.cinemaCityApiPort = cinemaCityApiPort;
        this.multikinoApiPort = multikinoApiPort;
        this.movieRepository = movieRepository;
        this.cinemaRepository = cinemaRepository;
        this.showingRepository = showingRepository;
    }

    public void processShowings() {
        log.info("Processing showings...");

        saveHeliosShowings();
        saveCinemaCityShowings();
        saveMultikinoShowings();
    }

    private void saveMultikinoShowings() {
        // one movie
        var showings = multikinoApiPort.fetchShowingsData().result().getFirst();
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

    private void saveCinemaCityShowings() {
        // only one day
        var events = cinemaCityApiPort.fetchMoviesData().body().events();
        events.forEach(showing -> {
            if (showingRepository.findByExternalId(showing.id().toString()).isEmpty()) {
                Optional<Movie> movie = movieRepository.findByCinemaCityId(showing.filmId());
                Optional<Cinema> cinema = cinemaRepository.findByExternalId(showing.cinemaId().toString());
                showingRepository.save(new Showing(showing.id().toString(),
                                                   cinema.get(),
                                                   movie.get(),
                                                   showing.eventDateTime()));
            }
        });
    }

    private void saveHeliosShowings() {
        // foreach cinema
        ShowingsRootDto showingsRootDto = heliosApiPort.fetchShowingsData();
        List<ShowingDto>
                flatShowings =
                showingsRootDto.data().screenings().entrySet().stream().flatMap(entry -> {
                    var date = entry.getKey();
                    var showingsOnDate = entry.getValue();
                    return showingsOnDate.entrySet().stream().flatMap(movieEntry -> {
                        var eventId = movieEntry.getKey();
                        var showings = movieEntry.getValue();
                        return showings.screenings().stream().map(
                                screeningDetails -> new ShowingDto(eventId, date, screeningDetails));
                    });
                }).toList();
        flatShowings.forEach(showing -> {
            ScreeningEntryDto screeningDetails = showing.screeningDetails();
            if (showingRepository.findByExternalId(screeningDetails.sourceId()).isEmpty()) {
                var id = showing.eventId();
                if (id.startsWith("m")) {
                    id = id.substring(1);
                    Optional<Movie> movie = movieRepository.findByHeliosId(Integer.parseInt(id));
                    Optional<Cinema> cinema =
                            cinemaRepository.findByExternalId("2");// is from path
                    showingRepository.save(new Showing(screeningDetails.sourceId(),
                                                       cinema.get(),
                                                       movie.get(),
                                                       screeningDetails.timeFrom()));
                }
            }
        });
    }
}
