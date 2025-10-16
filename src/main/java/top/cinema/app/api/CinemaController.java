package top.cinema.app.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.cinema.app.dao.CinemaRepository;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.dto.model.CinemaSummaryDto;
import top.cinema.app.dto.model.CinemaWithShowingsDto;
import top.cinema.app.entities.core.Cinema;
import top.cinema.app.entities.core.Showing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cinema")
@CrossOrigin(origins = "http://localhost:3000")
public class CinemaController {

    private final CinemaRepository cinemaRepository;
    private final ShowingRepository showingRepository;

    public CinemaController(CinemaRepository cinemaRepository, ShowingRepository showingRepository) {
        this.cinemaRepository = cinemaRepository;
        this.showingRepository = showingRepository;
    }

    @GetMapping
    public List<CinemaSummaryDto> getCinemas() {
        return cinemaRepository.findAll().stream().map(Cinema::toSummaryDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CinemaWithShowingsDto> getCinemaById(@PathVariable Integer id) {
        Optional<Cinema> cinemaOptional = cinemaRepository.findById(id);
        return cinemaOptional
                .map(cinema -> {
                    List<Showing> showings = showingRepository.findByCinemaAndShowingTimeAfter(cinema, LocalDateTime.now());
                    return ResponseEntity.ok(cinema.toWithShowingsDto(showings));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
