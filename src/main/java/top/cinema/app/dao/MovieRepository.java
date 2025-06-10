package top.cinema.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.cinema.app.entities.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
