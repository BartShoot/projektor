package top.cinema.app.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.cinema.app.entities.core.Cinema;
import top.cinema.app.entities.core.Movie;
import top.cinema.app.entities.core.Showing;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ShowingRepository extends JpaRepository<Showing, Integer> {
    Optional<Showing> findByExternalId(String externalId);

    Page<Showing> findByCinemaAndShowingTimeAfter(Cinema cinema, LocalDateTime localDateTime, Pageable pageable);

    Page<Showing> findByMovieAndShowingTimeAfter(Movie movie, LocalDateTime now, Pageable pageable);
}
