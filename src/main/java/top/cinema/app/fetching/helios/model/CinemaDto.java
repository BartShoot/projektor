package top.cinema.app.fetching.helios.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record CinemaDto(
        String name,
        int id,
        String sourceId,
        String header,
        String slug,
        int screensNumber,
        int seatsNumber,
        int seatsForDisabledNumber,
        String fanpageUrl,
        @JsonProperty("isDream") boolean isDream,
        String adsPrefix,
        ContactDto contact,
        LocationDto location,
        MainPhotoDto mainPhoto
) {}