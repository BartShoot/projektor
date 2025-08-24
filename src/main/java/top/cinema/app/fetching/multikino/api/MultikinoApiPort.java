package top.cinema.app.fetching.multikino.api;

import top.cinema.app.fetching.multikino.model.MultikinoCinemasRootDto;
import top.cinema.app.fetching.multikino.model.MultikinoMoviesRootDto;
import top.cinema.app.fetching.multikino.model.MultikinoShowingsRootDto;

public interface MultikinoApiPort {
    MultikinoCinemasRootDto fetchCinemasData();

    MultikinoMoviesRootDto fetchMoviesData();

    MultikinoShowingsRootDto fetchShowingsData();
}
