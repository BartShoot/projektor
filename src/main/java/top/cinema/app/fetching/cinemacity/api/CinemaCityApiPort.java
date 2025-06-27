package top.cinema.app.fetching.cinemacity.api;

import top.cinema.app.fetching.cinemacity.model.CinemaCityCinemasRootDto;

public interface CinemaCityApiPort {
    CinemaCityCinemasRootDto fetchCinemasData();
}
