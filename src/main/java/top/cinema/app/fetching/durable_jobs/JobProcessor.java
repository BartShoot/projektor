package top.cinema.app.fetching.durable_jobs;

import java.util.Collection;

public interface JobProcessor {

    void process(Collection<Job> jobs);
}
