package top.cinema.app.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.cinema.app.dao.CinemaRepository;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.dto.CinemaFront;
import top.cinema.app.dto.ShowingFront;
import top.cinema.app.entities.core.Cinema;
import top.cinema.app.entities.core.Showing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public List<CinemaFront> getCinemas() {
        return cinemaRepository.findAll().stream().map(Cinema::toFrontWithCity).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CinemaFront> getCinemaById(@PathVariable Integer id) {
        Optional<Cinema> cinemaOptional = cinemaRepository.findById(id);
        return cinemaOptional.map(cinema -> ResponseEntity.ok(
                cinema.toFrontWithShowing(
                        showingRepository.findByCinemaAndShowingTimeAfter(cinema, LocalDateTime.now())))).orElseGet(
                () -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/showing")
    public ResponseEntity<List<ShowingFront>> getCinemaShowings(@PathVariable Integer id) {
        Optional<Cinema> cinemaOptional = cinemaRepository.findById(id);
        if (cinemaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return cinemaOptional.map(
                cinema -> ResponseEntity.ok(
                        showingRepository.findByCinemaAndShowingTimeAfter(cinema, LocalDateTime.now()).stream().map(
                                Showing::toFront).toList())).orElseGet(
                () -> ResponseEntity.notFound().build());
    }
}
