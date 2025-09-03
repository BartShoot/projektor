package top.cinema.app.fetching.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.cinema.app.dao.CinemaRepository;
import top.cinema.app.dao.CityRepository;
import top.cinema.app.entities.Cinema;
import top.cinema.app.fetching.cinemacity.api.CinemaCityApiPort;
import top.cinema.app.fetching.helios.api.HeliosApiPort;
import top.cinema.app.fetching.multikino.api.MultikinoApiPort;
import top.cinema.app.model.CinemaChain;

@Service
public class CinemaSaver {

    private static final Logger log = LoggerFactory.getLogger(CinemaSaver.class);

    private final CinemaRepository cinemaRepository;
    private final CityRepository cityRepository;
    private final HeliosApiPort heliosApiPort;
    private final CinemaCityApiPort cinemaCityApiPort;
    private final MultikinoApiPort multikinoApiPort;
    private final CitySaver citySaver;

    public CinemaSaver(CinemaRepository cinemaRepository,
                       CityRepository cityRepository,
                       HeliosApiPort heliosApiPort,
                       CinemaCityApiPort cinemaCityApiPort,
                       MultikinoApiPort multikinoApiPort,
                       CitySaver citySaver) {
        this.cinemaRepository = cinemaRepository;
        this.cityRepository = cityRepository;
        this.heliosApiPort = heliosApiPort;
        this.cinemaCityApiPort = cinemaCityApiPort;
        this.multikinoApiPort = multikinoApiPort;
        this.citySaver = citySaver;
    }

    public void processCinemas() {
        log.info("Processing cinemas...");

        // Helios
        var heliosCinemas = heliosApiPort.fetchCinemasData();
        heliosCinemas.data().forEach(heliosCinema -> {
            cityRepository.findByName(heliosCinema.location().city()).ifPresentOrElse(city -> {
                if (cinemaRepository.findByNameAndCinemaChain(heliosCinema.name(), CinemaChain.HELIOS).isEmpty()) {
                    Cinema cinema = new Cinema(heliosCinema.name(),
                                               heliosCinema.location().street(),
                                               String.valueOf(heliosCinema.id()),
                                               CinemaChain.HELIOS,
                                               city);
                    cinemaRepository.save(cinema);
                    log.info("Saved Helios cinema: {} in city {}", heliosCinema.name(), city.getName());
                }
            }, () -> log.warn("City not found for Helios cinema: {}", heliosCinema.name()));
        });

        // Cinema City
        var cinemaCityCinemas = cinemaCityApiPort.fetchCinemasData();
        cinemaCityCinemas.body().cinemas().forEach(cinemaCityCinema -> {
            cityRepository.findByName(cinemaCityCinema.addressInfo().city()).ifPresentOrElse(city -> {
                if (cinemaRepository.findByNameAndCinemaChain(cinemaCityCinema.displayName(), CinemaChain.CINEMA_CITY)
                                    .isEmpty()) {
                    Cinema cinema = new Cinema(cinemaCityCinema.displayName(),
                                               cinemaCityCinema.addressInfo().address1(),
                                               cinemaCityCinema.id(),
                                               CinemaChain.CINEMA_CITY,
                                               city);
                    cinemaRepository.save(cinema);
                    log.info("Saved Cinema City cinema: {} in city {}", cinemaCityCinema.displayName(), city.getName());
                }
            }, () -> log.warn("City not found for Cinema City cinema: {}", cinemaCityCinema.displayName()));
        });

        // Multikino
        var multikinoCinemas = multikinoApiPort.fetchCinemasData();
        multikinoCinemas.result().forEach(group -> {
            group.cinemas().forEach(multikinoCinema -> {
                String cityName = citySaver.getCityWhitelist().stream()
                                           .filter(multikinoCinema.cinemaName()::startsWith)
                                           .findFirst()
                                           .orElse(null);
                // This is a placeholder for the address, as it's not available in the cinemas.json
                String address = "";
                if (cityName != null) {
                    cityRepository.findByName(cityName).ifPresentOrElse(city -> {
                        log.info("Attempting to save Multikino cinema: {} in city {}",
                                 multikinoCinema.cinemaName(),
                                 city.getName());
                        if (cinemaRepository.findByNameAndCinemaChain(multikinoCinema.cinemaName(),
                                                                      CinemaChain.MULTIKINO).isEmpty()) {
                            Cinema cinema = new Cinema(multikinoCinema.cinemaName(),
                                                       "",
                                                       multikinoCinema.cinemaId(),
                                                       CinemaChain.MULTIKINO,
                                                       city);
                            cinemaRepository.save(cinema);
                            log.info("Saved Multikino cinema: {} in city {}",
                                     multikinoCinema.cinemaName(),
                                     city.getName());
                        } else {
                            log.info("Multikino cinema already exists: {} in city {}",
                                     multikinoCinema.cinemaName(),
                                     city.getName());
                        }
                    }, () -> log.warn("City not found in DB for Multikino cinema: {}", multikinoCinema.cinemaName()));
                } else {
                    log.warn("City name could not be extracted for Multikino cinema: {}", multikinoCinema.cinemaName());
                }
            });
        });
    }
}
