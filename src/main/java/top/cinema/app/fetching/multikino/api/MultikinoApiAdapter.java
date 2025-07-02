package top.cinema.app.fetching.multikino.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import top.cinema.app.fetching.multikino.model.MultikinoCinemasRootDto;
import top.cinema.app.fetching.multikino.model.MultikinoMoviesRootDto;

import java.io.InputStream;

@Service
public class MultikinoApiAdapter implements MultikinoApiPort {

    private final ObjectMapper objectMapper;
    private static final String CINEMAS_FILE_PATH = "/multikino/cinemas.json";
    private static final String MOVIES_FILE_PATH = "/multikino/movies.json";

    public MultikinoApiAdapter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public MultikinoCinemasRootDto fetchCinemasData() {
        try (InputStream inputStream = MultikinoApiAdapter.class.getResourceAsStream(CINEMAS_FILE_PATH)) {
            if (inputStream == null) {
                throw new RuntimeException("Cannot find the cinemas JSON file: " + CINEMAS_FILE_PATH);
            }
            return objectMapper.readValue(inputStream, MultikinoCinemasRootDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read or parse cinemas JSON file", e);
        }
    }

    @Override
    public MultikinoMoviesRootDto fetchMoviesData() {
        try (InputStream inputStream = MultikinoApiAdapter.class.getResourceAsStream(MOVIES_FILE_PATH)) {
            if (inputStream == null) {
                throw new RuntimeException("Cannot find the movies JSON file: " + MOVIES_FILE_PATH);
            }
            return objectMapper.readValue(inputStream, MultikinoMoviesRootDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read or parse movies JSON file", e);
        }
    }
}
