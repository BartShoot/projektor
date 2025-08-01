package top.cinema.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.cinema.app.entities.Cinema;
import top.cinema.app.model.CinemaChain;

import java.util.Optional;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
    Optional<Cinema> findByNameAndCinemaChain(String name, CinemaChain cinemaChain);

    Optional<Cinema> findByExternalId(String externalId);
}
