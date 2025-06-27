package top.cinema.app.service;


import org.springframework.stereotype.Component;
import top.cinema.app.dao.CityRepository;
import top.cinema.app.entities.City;
import top.cinema.app.fetching.helios.HeliosService;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Component
public class CitySaver {
    private final HeliosService heliosService;
    private final CityRepository cityDAO;

    public CitySaver(HeliosService heliosService, CityRepository cityDAO) {
        this.heliosService = heliosService;
        this.cityDAO = cityDAO;
    }

    public void processCities() {
        var cinemas = heliosService.getAllCinemas();
        PrintWriter consoleOut = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8));
        cinemas.forEach(cinema -> {
            String city = cinema.location().city();

            var cityFromDb = cityDAO.findByName(city);
            if (cityFromDb.isEmpty()) {
                cityDAO.save(new City(city));
            }
        });
    }
}
