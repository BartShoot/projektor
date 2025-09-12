package top.cinema.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.cinema.app.entities.core.Cinema;
import top.cinema.app.entities.core.Showing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowingRepository extends JpaRepository<Showing, Integer> {
    Optional<Showing> findByExternalId(String externalId);

    List<Showing> findByCinemaAndShowingTimeAfter(Cinema cinema, LocalDateTime localDateTime);
}
