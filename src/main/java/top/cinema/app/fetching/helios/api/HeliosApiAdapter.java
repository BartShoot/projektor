package top.cinema.app.fetching.helios.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import top.cinema.app.fetching.helios.model.CinemasRootDto;
import top.cinema.app.fetching.helios.model.ShowingsRootDto;

import java.io.InputStream;

@Service
public class HeliosApiAdapter implements HeliosApiPort {

    private final ObjectMapper objectMapper;
    private static final String CINEMAS_FILE_PATH = "/helios/cinemas.json";
    private static final String SHOWINGS_FILE_PATH = "/helios/showings.json";

    public HeliosApiAdapter() {
        this.objectMapper = new ObjectMapper();
        // objectMapper.registerModule(new JavaTimeModule()); // If you use LocalDateTime in DTOs
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public CinemasRootDto fetchCinemasData() {
        try (InputStream inputStream = HeliosApiAdapter.class.getResourceAsStream(CINEMAS_FILE_PATH)) {
            if (inputStream == null) {
                throw new RuntimeException("Cannot find the cinemas JSON file: " + CINEMAS_FILE_PATH);
            }
            return objectMapper.readValue(inputStream, CinemasRootDto.class);
        } catch (Exception e) {
            // Consider a more specific exception strategy
            throw new RuntimeException("Failed to read or parse cinemas JSON file", e);
        }
    }

    @Override
    public ShowingsRootDto fetchShowingsData() {
        try (InputStream inputStream = HeliosApiAdapter.class.getResourceAsStream(SHOWINGS_FILE_PATH)) {
            if (inputStream == null) {
                throw new RuntimeException("Cannot find the showings JSON file: " + SHOWINGS_FILE_PATH);
            }
            return objectMapper.readValue(inputStream, ShowingsRootDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read or parse showings JSON file", e);
        }
    }
}
