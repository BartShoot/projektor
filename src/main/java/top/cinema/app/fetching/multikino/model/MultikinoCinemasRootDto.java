package top.cinema.app.fetching.multikino.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MultikinoCinemasRootDto(
        @JsonProperty("result")
        List<MultikinoCinemasGroupDto> result
) {
}
