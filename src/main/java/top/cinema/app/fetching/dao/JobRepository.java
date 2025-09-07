package top.cinema.app.fetching.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.cinema.app.fetching.durable_jobs.Job;
import top.cinema.app.model.CinemaChain;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    List<Job> findByStatusAndCinemaChain(Job.Status status,
                                         CinemaChain cinemaChain,
                                         Pageable pageable);
}
