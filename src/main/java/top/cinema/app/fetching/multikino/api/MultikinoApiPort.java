package top.cinema.app.fetching.multikino.api;

import top.cinema.app.fetching.multikino.model.MultikinoCinemasRootDto;

public interface MultikinoApiPort {
    MultikinoCinemasRootDto fetchCinemasData();
}
