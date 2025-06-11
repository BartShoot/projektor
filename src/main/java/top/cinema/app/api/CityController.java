package top.cinema.app.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.cinema.app.dao.CityRepository;
import top.cinema.app.dto.CinemaFront;
import top.cinema.app.dto.CityFront;
import top.cinema.app.entities.Cinema;
import top.cinema.app.entities.City;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/city")
@CrossOrigin(origins = "http://localhost:3000")
public class CityController {

    private final CityRepository cityRepository;

    public CityController(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @GetMapping
    public ResponseEntity<List<CityFront>> getAllCities() {
        return ResponseEntity.ok(cityRepository.findAll().stream().map(City::toFront).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityFront> getCityById(@PathVariable Integer id) {
        Optional<City> cityOptional = cityRepository.findById(id);
        return cityOptional.map(city -> ResponseEntity.ok(city.toFrontWithCinemas()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/cinema")
    public ResponseEntity<List<CinemaFront>> getCityCinemas(@PathVariable Integer id) {
        Optional<City> cityOptional = cityRepository.findById(id);
        if (cityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return cityOptional.map(
                ci -> ResponseEntity.ok(ci.getCinemas().stream().map(Cinema::toFront).toList())).orElseGet(
                () -> ResponseEntity.notFound().build());
    }
}