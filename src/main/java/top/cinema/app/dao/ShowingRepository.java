package top.cinema.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.cinema.app.entities.Showing;

@Repository
public interface ShowingRepository extends JpaRepository<Showing, Integer> {
}
