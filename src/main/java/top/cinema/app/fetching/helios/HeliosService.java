package top.cinema.app.fetching.helios;

import top.cinema.app.fetching.helios.api.HeliosApiPort;
import top.cinema.app.fetching.helios.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HeliosService {

    private final HeliosApiPort heliosApiPort;

    public HeliosService(HeliosApiPort heliosApiPort) {
        this.heliosApiPort = heliosApiPort;
    }

    public List<CinemaDto> getAllCinemas() {
        CinemasRootDto cinemasData = heliosApiPort.fetchCinemasData();
        return cinemasData != null ? cinemasData.data() : Collections.emptyList();
    }

    public List<CinemaDto> findCinemasByCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            return Collections.emptyList();
        }
        CinemasRootDto cinemasData = heliosApiPort.fetchCinemasData();
        if (cinemasData == null || cinemasData.data() == null) {
            return Collections.emptyList();
        }
        return cinemasData.data().stream()
                .filter(cinema -> cinema.location() != null && city.equalsIgnoreCase(cinema.location().city()))
                .collect(Collectors.toList());
    }

    public ShowingsDataPayloadDto getFullShowingsData() {
        ShowingsRootDto showingsRoot = heliosApiPort.fetchShowingsData();
        return showingsRoot != null ? showingsRoot.data() : null;
    }

    public Map<String, DailyMovieScreeningsDto> getScreeningsForDate(LocalDate date) {
        ShowingsRootDto showingsRoot = heliosApiPort.fetchShowingsData();
        if (showingsRoot == null || showingsRoot.data() == null || showingsRoot.data().screenings() == null) {
            return Collections.emptyMap();
        }
        String dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        return showingsRoot.data().screenings().getOrDefault(dateString, Collections.emptyMap());
    }

    public MovieDetailsDto getMovieDetails(String movieId) {
        ShowingsRootDto showingsRoot = heliosApiPort.fetchShowingsData();
        if (showingsRoot == null || showingsRoot.data() == null || showingsRoot.data().movies() == null) {
            return null;
        }
        return showingsRoot.data().movies().get(movieId);
    }
}
