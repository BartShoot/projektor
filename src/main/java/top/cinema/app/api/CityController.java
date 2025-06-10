package top.cinema.app.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.cinema.app.dao.CinemaRepository;
import top.cinema.app.dao.CityRepository;
import top.cinema.app.entities.Cinema;
import top.cinema.app.entities.City;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/city")
public class CityController {

    private final CityRepository cityRepository;
    private final CinemaRepository cinemaRepository;

    private static final List<City> cities = new ArrayList<>();

    public CityController(CityRepository cityRepository, CinemaRepository cinemaRepository) {
        this.cityRepository = cityRepository;
        this.cinemaRepository = cinemaRepository;
    }

    @GetMapping
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Integer id) {
        Optional<City> cityOptional = cityRepository.findById(id);
        return cityOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/cinema")
    public List<Cinema> getCinemas(){
        return cinemaRepository.findAll();
    }
}