package top.cinema.app.fetching.helios.api;

import top.cinema.app.fetching.helios.model.CinemasRootDto;
import top.cinema.app.fetching.helios.model.ShowingsRootDto;

public interface HeliosApiPort {
    CinemasRootDto fetchCinemasData();

    ShowingsRootDto fetchShowingsData();
}