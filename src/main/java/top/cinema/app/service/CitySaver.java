package top.cinema.app.service;

import org.springframework.stereotype.Component;
import top.cinema.app.dao.CityRepository;
import top.cinema.app.entities.City;
import top.cinema.app.fetching.cinemacity.api.CinemaCityApiPort;
import top.cinema.app.fetching.helios.api.HeliosApiPort;
import top.cinema.app.fetching.multikino.api.MultikinoApiPort;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CitySaver {
    private final HeliosApiPort heliosApiPort;
    private final CinemaCityApiPort cinemaCityApiPort;
    private final MultikinoApiPort multikinoApiPort;
    private final CityRepository cityDAO;
    private final Set<String> cityWhitelist;

    public CitySaver(HeliosApiPort heliosApiPort, CinemaCityApiPort cinemaCityApiPort, MultikinoApiPort multikinoApiPort, CityRepository cityDAO) {
        this.heliosApiPort = heliosApiPort;
        this.cinemaCityApiPort = cinemaCityApiPort;
        this.multikinoApiPort = multikinoApiPort;
        this.cityDAO = cityDAO;
        this.cityWhitelist = loadCityWhitelist();
    }

    private Set<String> loadCityWhitelist() {
        try (var inputStream = getClass().getResourceAsStream("/multikino/cities_whitelist.txt");
             var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load city whitelist", e);
        }
    }

    public void processCities() {
        Set<String> allCities = new HashSet<>();

        // Helios
        var heliosCinemas = heliosApiPort.fetchCinemasData();
        heliosCinemas.data().forEach(cinema -> allCities.add(cinema.location().city()));

        // Cinema City
        var cinemaCityCinemas = cinemaCityApiPort.fetchCinemasData();
        cinemaCityCinemas.body().cinemas().forEach(cinema -> allCities.add(cinema.addressInfo().city()));

        // Multikino
        var multikinoCinemas = multikinoApiPort.fetchCinemasData();
        multikinoCinemas.result().forEach(group -> group.cinemas().forEach(cinema -> {
            cityWhitelist.stream()
                    .filter(cinema.cinemaName()::startsWith)
                    .findFirst()
                    .ifPresentOrElse(
                            allCities::add,
                            () -> System.out.println("WARN: City for cinema '" + cinema.cinemaName() + "' not found in whitelist.")
                    );
        }));

        PrintWriter consoleOut = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8));
        allCities.forEach(city -> {
            var cityFromDb = cityDAO.findByName(city);
            if (cityFromDb.isEmpty()) {
                cityDAO.save(new City(city));
                consoleOut.println("Saved new city: " + city);
            }
        });
        consoleOut.flush();
    }
}
