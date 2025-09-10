package top.cinema.app.fetching.helios.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import top.cinema.app.fetching.helios.model.CinemasRootDto;
import top.cinema.app.fetching.helios.model.ShowingsRootDto;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Profile("!live")
public class HeliosStaticAdapter implements HeliosApiPort {

    private final ObjectMapper objectMapper;
    private static final String CINEMAS_FILE_PATH = "/helios/cinemas.json";
    private static final String SHOWINGS_FILE_PATH = "/helios/showings.json";

    public HeliosStaticAdapter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        objectMapper.registerModule(javaTimeModule); // If you use LocalDateTime in DTOs
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public CinemasRootDto fetchCinemasData() {
        try (InputStream inputStream = HeliosStaticAdapter.class.getResourceAsStream(CINEMAS_FILE_PATH)) {
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
    public ShowingsRootDto fetchShowingsData(Integer cinemaId) {
        try (InputStream inputStream = HeliosStaticAdapter.class.getResourceAsStream(SHOWINGS_FILE_PATH)) {
            if (inputStream == null) {
                throw new RuntimeException("Cannot find the showings JSON file: " + SHOWINGS_FILE_PATH);
            }
            return objectMapper.readValue(inputStream, ShowingsRootDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read or parse showings JSON file", e);
        }
    }
}
