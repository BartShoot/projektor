package top.cinema.app.fetching.helios.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PhotoMetaDto(
        String name,
        long size,
        int width,
        int height,
        String extension,
        @JsonProperty("mime_type") String mimeType
) {}
