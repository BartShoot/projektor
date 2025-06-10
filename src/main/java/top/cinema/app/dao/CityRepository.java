package top.cinema.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.cinema.app.entities.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
}
