package top.cinema.app.fetching.multikino.api;

import top.cinema.app.fetching.multikino.model.MultikinoCinemasRootDto;
import top.cinema.app.fetching.multikino.model.MultikinoMoviesRootDto;

public interface MultikinoApiPort {
    MultikinoCinemasRootDto fetchCinemasData();

    MultikinoMoviesRootDto fetchMoviesData();
}
