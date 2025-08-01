package top.cinema.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.cinema.app.entities.Showing;

import java.util.Optional;

@Repository
public interface ShowingRepository extends JpaRepository<Showing, Integer> {
    Optional<Showing> findByExternalId(String externalId);
}
