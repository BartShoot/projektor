package top.cinema.app.service;

import org.springframework.stereotype.Service;
import top.cinema.app.fetching.cinemacity.api.CinemaCityApiPort;
import top.cinema.app.fetching.helios.api.HeliosApiPort;
import top.cinema.app.fetching.multikino.api.MultikinoApiPort;

@Service
public class ShowingSaver {

    private final HeliosApiPort heliosApiPort;
    private final CinemaCityApiPort cinemaCityApiPort;
    private final MultikinoApiPort multikinoApiPort;

    public ShowingSaver(HeliosApiPort heliosApiPort, CinemaCityApiPort cinemaCityApiPort, MultikinoApiPort multikinoApiPort) {
        this.heliosApiPort = heliosApiPort;
        this.cinemaCityApiPort = cinemaCityApiPort;
        this.multikinoApiPort = multikinoApiPort;
    }
}
