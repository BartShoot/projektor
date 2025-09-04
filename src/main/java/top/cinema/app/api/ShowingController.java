package top.cinema.app.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.cinema.app.dao.ShowingRepository;
import top.cinema.app.dto.ShowingFront;
import top.cinema.app.entities.core.Showing;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/showing")
@CrossOrigin(origins = "http://localhost:3000")
public class ShowingController {

    private final ShowingRepository showingRepository;

    public ShowingController(ShowingRepository showingRepository) {
        this.showingRepository = showingRepository;
    }

    @GetMapping
    public ResponseEntity<List<ShowingFront>> getAllShowings() {
        return ResponseEntity.ok(showingRepository.findAll().stream().map(Showing::toFrontWithCinema).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowingFront> getShowingById(@PathVariable Integer id) {
        Optional<Showing> showingOptional = showingRepository.findById(id);
        return showingOptional.map(showing -> ResponseEntity.ok(showing.toFrontWithCinema())).orElseGet(
                ResponseEntity.notFound()::build);
    }
}
