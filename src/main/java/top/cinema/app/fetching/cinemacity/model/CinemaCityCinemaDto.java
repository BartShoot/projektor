package top.cinema.app.fetching.cinemacity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CinemaCityCinemaDto(
        String id,
        String displayName,
        @JsonProperty("addressInfo")
        CinemaCityAddressInfoDto addressInfo
) {
}
