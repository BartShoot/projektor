package top.cinema.app.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.cinema.app.entities.core.Movie;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Optional<Movie> findByTitle(String title);

    Optional<Movie> findByNormalizedTitle(String title);

    Optional<Movie> findByCinemaCityId(String id);

    Optional<Movie> findByHeliosId(int id);

    Optional<Movie> findByMultikinoId(String id);

    @Query("SELECT m FROM Movie m JOIN m.showings s WHERE s.showingTime > CURRENT_TIMESTAMP GROUP BY m")
    Page<Movie> findMoviesWithFutureShowings(Pageable pageable);
}
