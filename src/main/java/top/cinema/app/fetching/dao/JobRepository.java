package top.cinema.app.fetching.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.cinema.app.fetching.durable_jobs.Job;
import top.cinema.app.model.CinemaChain;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    List<Job> findByStatusAndCinemaChain(Job.Status status,
                                         CinemaChain cinemaChain,
                                         Pageable pageable);

    @Query("SELECT j FROM Job j " +
            "WHERE j.cinemaChain = :cinemaChain " +
            "AND j.type = :type " +
            "AND j.status = :status " +
            "ORDER BY j.lastUpdateDate DESC " +
            "LIMIT 1")
    Optional<Job> findLastJob(CinemaChain cinemaChain, Job.Type type, Job.Status status);
}
