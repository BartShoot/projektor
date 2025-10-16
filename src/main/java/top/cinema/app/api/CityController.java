package top.cinema.app.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.cinema.app.dao.CityRepository;
import top.cinema.app.dto.model.CitySummaryDto;
import top.cinema.app.dto.model.CityWithCinemasDto;
import top.cinema.app.entities.core.City;

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
    public ResponseEntity<List<CitySummaryDto>> getAllCities() {
        return ResponseEntity.ok(cityRepository.findAll().stream().map(City::toSummaryDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityWithCinemasDto> getCityById(@PathVariable Integer id) {
        Optional<City> cityOptional = cityRepository.findById(id);
        return cityOptional.map(city -> ResponseEntity.ok(city.toWithCinemasDto()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}