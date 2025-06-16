package top.cinema.app.fetching.helios.model;

public record ShowingsRootDto(
        int status,
        ShowingsDataPayloadDto data
) {}
