package top.cinema.app.fetching.cinemacity.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import top.cinema.app.fetching.cinemacity.model.CinemaCityCinemasRootDto;
import top.cinema.app.fetching.cinemacity.model.CinemaCityMoviesRootDto;

import java.io.InputStream;

@Service
public class CinemaCityApiAdapter implements CinemaCityApiPort {

    private final ObjectMapper objectMapper;
    private static final String CINEMAS_FILE_PATH = "/cinema-city/cinemas.json";
    private static final String MOVIES_FILE_PATH = "/cinema-city/movies.json";

    public CinemaCityApiAdapter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public CinemaCityCinemasRootDto fetchCinemasData() {
        try (InputStream inputStream = CinemaCityApiAdapter.class.getResourceAsStream(CINEMAS_FILE_PATH)) {
            if (inputStream == null) {
                throw new RuntimeException("Cannot find the cinemas JSON file: " + CINEMAS_FILE_PATH);
            }
            return objectMapper.readValue(inputStream, CinemaCityCinemasRootDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read or parse cinemas JSON file", e);
        }
    }

    @Override
    public CinemaCityMoviesRootDto fetchMoviesData() {
        try (InputStream inputStream = CinemaCityApiAdapter.class.getResourceAsStream(MOVIES_FILE_PATH)) {
            if (inputStream == null) {
                throw new RuntimeException("Cannot find the movies JSON file: " + MOVIES_FILE_PATH);
            }
            return objectMapper.readValue(inputStream, CinemaCityMoviesRootDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read or parse movies JSON file", e);
        }
    }
}
