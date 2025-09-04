package top.cinema.app.fetching.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.cinema.app.entities.durable_jobs.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
}
