package top.cinema.app.fetching.cinemacity.api;

import top.cinema.app.fetching.cinemacity.model.CinemaCityCinemasRootDto;
import top.cinema.app.fetching.cinemacity.model.CinemaCityMoviesRootDto;

import java.time.LocalDate;

public interface CinemaCityApiPort {
    CinemaCityCinemasRootDto fetchCinemasData();

    CinemaCityMoviesRootDto fetchMoviesData(Integer cinemaId, LocalDate now);
}
