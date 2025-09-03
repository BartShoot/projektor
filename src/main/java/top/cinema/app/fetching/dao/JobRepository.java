package top.cinema.app.fetching.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.cinema.app.fetching.durable_jobs.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
}
