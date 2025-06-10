package top.cinema.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.cinema.app.entities.Cinema;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
}
