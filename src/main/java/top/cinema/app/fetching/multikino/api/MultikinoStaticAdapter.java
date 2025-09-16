package top.cinema.app.fetching.multikino.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import top.cinema.app.fetching.multikino.model.MultikinoCinemasRootDto;
import top.cinema.app.fetching.multikino.model.MultikinoMoviesRootDto;
import top.cinema.app.fetching.multikino.model.MultikinoShowingsRootDto;

import java.io.InputStream;

@Service
@Profile("!live")
public class MultikinoStaticAdapter implements MultikinoApiPort {

    private final ObjectMapper objectMapper;
    private static final String CINEMAS_FILE_PATH = "/multikino/cinemas.json";
    private static final String MOVIES_FILE_PATH = "/multikino/movies.json";
    private static final String SHOWINGS_FILE_PATH = "/multikino/showings.json";

    public MultikinoStaticAdapter() {
        this.objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public MultikinoCinemasRootDto fetchCinemasData() {
        try (InputStream inputStream = MultikinoStaticAdapter.class.getResourceAsStream(CINEMAS_FILE_PATH)) {
            if (inputStream == null) {
                throw new RuntimeException("Cannot find the cinemas JSON file: " + CINEMAS_FILE_PATH);
            }
            return objectMapper.readValue(inputStream, MultikinoCinemasRootDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read or parse cinemas JSON file", e);
        }
    }

    @Override
    public MultikinoMoviesRootDto fetchMoviesData(String cinemaId) {
        try (InputStream inputStream = MultikinoStaticAdapter.class.getResourceAsStream(MOVIES_FILE_PATH)) {
            if (inputStream == null) {
                throw new RuntimeException("Cannot find the movies JSON file: " + MOVIES_FILE_PATH);
            }
            return objectMapper.readValue(inputStream, MultikinoMoviesRootDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read or parse movies JSON file", e);
        }
    }

    @Override
    public MultikinoShowingsRootDto fetchShowingsData(String filmId, String cinemaId) {
        try (InputStream inputStream = MultikinoStaticAdapter.class.getResourceAsStream(SHOWINGS_FILE_PATH)) {
            if (inputStream == null) {
                throw new RuntimeException("Cannot find the showings JSON file: " + SHOWINGS_FILE_PATH);
            }
            return objectMapper.readValue(inputStream, MultikinoShowingsRootDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read or parse movies JSON file", e);
        }
    }
}
